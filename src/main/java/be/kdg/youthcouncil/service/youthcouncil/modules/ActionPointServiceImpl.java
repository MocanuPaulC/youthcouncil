package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class ActionPointServiceImpl implements ActionPointService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;
	private final ActionPointRepository actionPointRepository;

	@Transactional (propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	@Override
	public EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto) {
		ActionPoint newActionPoint = modelMapper.map(editActionPointDto, ActionPoint.class);
		YouthCouncil youthCouncil = youthCouncilRepository.findById(youthCouncilId)
		                                                  .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + youthCouncilId + "  could not be found!"));

		ActionPoint oldActionPoint = youthCouncil.getActionPoint(actionPointId);
		if (oldActionPoint.equals(newActionPoint)) {
			logger.debug("ActionPoint is the same");
			return modelMapper.map(oldActionPoint, EditActionPointDto.class);
		}
		if (oldActionPoint.getIsDefault()) {
			logger.debug("ActionPoint is default");
			newActionPoint.setIsDefault(false);
		}
		newActionPoint.setOwningYouthCouncil(youthCouncil);
		oldActionPoint.setTitle(newActionPoint.getTitle());
		oldActionPoint.setDescription(newActionPoint.getDescription());
		oldActionPoint.setStatus(newActionPoint.getStatus());
		return modelMapper.map(actionPointRepository.save(newActionPoint), EditActionPointDto.class);
	}

	@Override
	public EditActionPointDto updateDefault(long actionPointId, EditActionPointDto editActionPointDto) {
		//FIXME implement me!
		return new EditActionPointDto();
	}

}
