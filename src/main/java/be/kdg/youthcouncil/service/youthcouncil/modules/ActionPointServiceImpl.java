package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPointBlock;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ModuleStatus;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.exceptions.ActionPointNotFoundException;
import be.kdg.youthcouncil.exceptions.InformativePageSetupMismatchException;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointBlockRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.ActionPointSubscriptionRepository;
import be.kdg.youthcouncil.utility.Notification;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ActionPointServiceImpl implements ActionPointService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;
	private final ActionPointRepository actionPointRepository;
	private final ActionPointBlockRepository actionPointBlockRepository;
	private final ThemeRepository themeRepository;
	private final UserRepository userRepository;
	private final ActionPointSubscriptionRepository actionPointSubscriptionRepository;

	@Transactional (propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	@Override
	public EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto) {
		ActionPoint newActionPoint = modelMapper.map(editActionPointDto, ActionPoint.class);
		YouthCouncil youthCouncil = youthCouncilRepository.findById(youthCouncilId)
		                                                  .orElseThrow(() -> new MunicipalityNotFoundException("The YouthCouncil " + youthCouncilId + "  could not be found!"));

		ActionPoint oldActionPoint = actionPointRepository.findById(actionPointId)
		                                                  .orElseThrow(() -> new ActionPointNotFoundException(actionPointId));
		//		ActionPoint oldActionPoint = youthCouncil.getActionPoint(actionPointId);
		if (oldActionPoint.equals(newActionPoint)) {
			logger.debug("ActionPoint is the same");
			return modelMapper.map(oldActionPoint, EditActionPointDto.class);
		}
		if (oldActionPoint.getIsDefault()) {
			logger.debug("ActionPoint is default");
			newActionPoint.setIsDefault(false);
			// Maybe not necessary
			youthCouncil.addActionPoint(newActionPoint);
			youthCouncilRepository.save(youthCouncil);
			return modelMapper.map(actionPointRepository.save(newActionPoint), EditActionPointDto.class);
		} else {
			logger.debug("ActionPoint is not default");
			oldActionPoint.setTitle(newActionPoint.getTitle());
			oldActionPoint.setDescription(newActionPoint.getDescription());
			oldActionPoint.setStatus(newActionPoint.getStatus());
			return modelMapper.map(actionPointRepository.save(oldActionPoint), EditActionPointDto.class);
		}
	}

	@Override
	@Transactional
	public ActionPoint save(String title, boolean isDefault, List<BlockDto> infoPageBlocksDto, Optional<String> municipality, String theme) {

		if (isDefault && municipality.isPresent()) {
			throw new InformativePageSetupMismatchException(municipality.get(), title);
		}
		SubTheme st = themeRepository.findAll()
		                             .stream()
		                             .flatMap(t -> t.getSubThemes().stream())
		                             .filter(s -> s.getSubTheme().equals(theme))
		                             .findFirst()
		                             .orElse(null);

		YouthCouncil owningYouthCouncil = youthCouncilRepository.findByMunicipalityName(municipality.get())
		                                                        .orElseThrow(() -> new MunicipalityNotFoundException(municipality.get()));

		ActionPoint newActionPoint = municipality.map(m -> actionPointRepository.findByTitleAndMunicipality(title, m))
		                                         .orElseGet(() -> actionPointRepository.findDefaultByTitle(title))
		                                         .orElse(new ActionPoint(
				                                         title,
				                                         st,
				                                         isDefault,
				                                         owningYouthCouncil
		                                         ));


		actionPointBlockRepository.deleteAll(newActionPoint.getActionPointBlocks());


		ActionPoint apToReturn = actionPointRepository.save(newActionPoint);

		newActionPoint.setActionPointBlocks(infoPageBlocksDto.stream().map(blockDto -> {
			ActionPointBlock block = modelMapper.map(blockDto, ActionPointBlock.class);
			block.setOwningActionPoint(newActionPoint);
			actionPointBlockRepository.save(block);
			return block;
		}).toList());
		owningYouthCouncil.addActionPoint(newActionPoint);
		youthCouncilRepository.save(owningYouthCouncil);
		return apToReturn;
	}

	@Override
	public List<BlockDto> findActionPointBlocks(Optional<String> municipality, long actionPointId) {
		List<ActionPointBlock> blocks;
		if (municipality.isEmpty()) {
			blocks = actionPointRepository.findActionPointBlocks(actionPointId);
		} else {
			blocks = actionPointRepository.findActionPointBlocks(municipality.get(), actionPointId);
		}
		return blocks.stream()
		             .map(block -> modelMapper.map(block, BlockDto.class))
		             .toList();
	}

	@Override
	public boolean existsByTitle(Optional<String> municipality, String title) {
		return municipality.map(s -> actionPointRepository.findByTitleAndMunicipality(title, s).isPresent())
		                   .orElseGet(() -> actionPointRepository.findDefaultByTitle(title).isPresent());
	}

	@Override
	public ActionPoint findById(long actionPointId) {
		return actionPointRepository.findById(actionPointId)
		                            .orElseThrow(() -> new ActionPointNotFoundException(actionPointId));
	}

	@Override
	public void setDisplay(long actionPointId, boolean isDisplayed) {
		ActionPoint actionPoint = actionPointRepository.findById(actionPointId)
		                                               .orElseThrow(() -> new ActionPointNotFoundException(actionPointId));
		if (isDisplayed) {
			actionPoint.setModuleStatus(ModuleStatus.DISPLAYED);
		} else {
			actionPoint.setModuleStatus(ModuleStatus.HIDDEN);
		}
		actionPointRepository.save(actionPoint);
	}

	@Transactional (readOnly = true)
	@Override
	public List<ActionPointSubscription> findAllSubscriptionsByActionPointId(long actionPointId) {
		List<ActionPointSubscription> subscriptions = actionPointRepository.findByIdWithSubscriptions(actionPointId)
		                                                                   .orElseThrow(() -> new ActionPointNotFoundException(actionPointId))
		                                                                   .getSubscriptions();

		addNotificationsToUsers(subscriptions);
		return subscriptions;
	}

	@Override
	public void subscribe(long userId, long actionPointId) {
		PlatformUser subscriber = userRepository.findById(userId)
		                                        .orElseThrow(() -> new ActionPointNotFoundException(userId));
		ActionPoint actionPoint = actionPointRepository.findById(actionPointId)
		                                               .orElseThrow(() -> new ActionPointNotFoundException(actionPointId));
		actionPointSubscriptionRepository.save(new ActionPointSubscription(subscriber, actionPoint));
	}

	@Override
	public void unsubscribe(long userId, long actionPointId) {
		PlatformUser subscriber = userRepository.findById(userId)
		                                        .orElseThrow(() -> new ActionPointNotFoundException(userId));
		ActionPoint actionPoint = actionPointRepository.findById(actionPointId)
		                                               .orElseThrow(() -> new ActionPointNotFoundException(actionPointId));
		actionPointSubscriptionRepository.findByActionPointAndSubscriber(actionPoint, subscriber)
		                                 .ifPresent(actionPointSubscriptionRepository::delete);
	}

	private void addNotificationsToUsers(List<ActionPointSubscription> subscriptions) {
		subscriptions.forEach(subscription -> addNotificationsToUser(subscription.getSubscriber(), subscription.getSubscriber()
		                                                                                                       .getNotifications()));

	}

	private void addNotificationsToUser(PlatformUser user, List<Notification> notifications) {
		user.setNotifications(new ArrayList<>());
		notifications.forEach(user::addNotification);
	}


}
