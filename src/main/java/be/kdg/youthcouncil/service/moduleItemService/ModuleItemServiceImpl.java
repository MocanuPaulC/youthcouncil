package be.kdg.youthcouncil.service.moduleItemService;

import be.kdg.youthcouncil.controllers.api.dto.EditActionPointDto;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.persistence.ModuleItemRepository;
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
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Transactional (propagation = Propagation.REQUIRED)
	@Override
	public EditActionPointDto updateActionPoint(long oldActionPointId, long youthCouncilId, EditActionPointDto editActionPointDto) {
		ActionPoint newActionPoint = modelMapper.map(editActionPointDto, ActionPoint.class);
		YouthCouncil youthCouncil = youthCouncilRepository.findById(youthCouncilId)
		                                                  .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + youthCouncilId + "  could not be found!"));

		ActionPoint oldActionPoint = youthCouncil.getActionPoint(oldActionPointId, ActionPoint.class);
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
	public void handleCallForIdeas(String idea, long moduleItemId, long userId, String theme) {
		logger.debug("Handling call for ideas");
		//TODO: implement
	}
}
