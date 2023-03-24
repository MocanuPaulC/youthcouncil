package be.kdg.youthcouncil.service.moduleItemService;

import be.kdg.youthcouncil.controllers.api.dto.EditActionPointDto;
import be.kdg.youthcouncil.controllers.api.dto.IdeaSubmissionDTO;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.moduleItems.CallForIdea;
import be.kdg.youthcouncil.domain.moduleItems.Idea;
import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.exceptions.CallForIdeaNotFound;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.exceptions.UserNotFound;
import be.kdg.youthcouncil.persistence.CallForIdeaRepository;
import be.kdg.youthcouncil.persistence.ModuleItemRepository;
import be.kdg.youthcouncil.persistence.UserRepository;
import be.kdg.youthcouncil.persistence.YouthCouncilRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ModuleItemServiceImpl implements ModuleItemService {
	private final ModelMapper modelMapper;
	private final ModuleItemRepository moduleItemRepository;
	private final YouthCouncilRepository youthCouncilRepository;

	private final CallForIdeaRepository callForIdeaRepository;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Transactional (propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	@Override
	public EditActionPointDto updateActionPoint(long oldActionPointId, long youthCouncilId, EditActionPointDto editActionPointDto) {
		ActionPoint newActionPoint = modelMapper.map(editActionPointDto, ActionPoint.class);
		YouthCouncil youthCouncil = youthCouncilRepository.findById(youthCouncilId)
		                                                  .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + youthCouncilId + "  could not be found!"));

		ActionPoint oldActionPoint = youthCouncil.getActionPoint(oldActionPointId);
		if (oldActionPoint.equals(newActionPoint)) {
			logger.debug("ActionPoint is the same");
			return modelMapper.map(oldActionPoint, EditActionPointDto.class);
		}
		if (oldActionPoint.isDefault()) {
			logger.debug("ActionPoint is default");
			newActionPoint.setDefault(false);
			newActionPoint = moduleItemRepository.save(newActionPoint);
			youthCouncil.addModuleItem(newActionPoint);
			youthCouncilRepository.save(youthCouncil);
			return modelMapper.map(moduleItemRepository.save(newActionPoint), EditActionPointDto.class);
		} else {
			logger.debug("ActionPoint is not default");
			oldActionPoint.setTitle(newActionPoint.getTitle());
			oldActionPoint.setDescription(newActionPoint.getDescription());
			oldActionPoint.setLabel(newActionPoint.getLabel());
			return modelMapper.map(moduleItemRepository.save(oldActionPoint), EditActionPointDto.class);
		}

	}

	@Override
	public List<ModuleItem> getAllModuleItems() {
		logger.debug("Getting all module items");
		return moduleItemRepository.findAll();
	}

	@Override
	public IdeaSubmissionDTO handleCallForIdeas(String idea, long callForIdeaId, long userId, String theme) {
		logger.debug("Handling call for ideas");
		// to Change
		Idea newIdea = new Idea(idea);
		System.out.println(newIdea);
		moduleItemRepository.save(newIdea);
		User user = userRepository.findByIdWithIdeas(userId).orElseThrow(() -> {throw new UserNotFound(userId);});
		user.addIdea(newIdea);
		userRepository.save(user);
		CallForIdea callForIdea = callForIdeaRepository.findById(callForIdeaId).orElseThrow(() -> {
			throw new CallForIdeaNotFound(callForIdeaId);
		});
		callForIdea.addIdea(newIdea);
		callForIdeaRepository.save(callForIdea);

		return new IdeaSubmissionDTO(idea);
	}
}
