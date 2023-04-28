package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;

public interface ActionPointService {

	EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto);

	EditActionPointDto updateDefault(long actionPointId, EditActionPointDto editActionPointDto);

	ActionPoint findById(long actionPointReactedOnId);

	void setDisplay(long actionPointId, boolean isDisplayed);
}
