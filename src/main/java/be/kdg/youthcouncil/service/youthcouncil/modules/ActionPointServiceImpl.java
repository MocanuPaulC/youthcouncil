package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ModuleStatus;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.exceptions.ActionPointNotFoundException;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
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


@AllArgsConstructor
@Service
public class ActionPointServiceImpl implements ActionPointService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;
	private final ActionPointRepository actionPointRepository;
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
	public EditActionPointDto updateDefault(long actionPointId, EditActionPointDto editActionPointDto) {
		//FIXME implement me!
		return new EditActionPointDto();
	}

	@Override
	public ActionPoint findById(long actionPointReactedOnId) {
		return actionPointRepository.findById(actionPointReactedOnId)
		                            .orElseThrow(() -> new ActionPointNotFoundException(actionPointReactedOnId));
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
