package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;

public interface ActionPointService {

	EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto);

	EditActionPointDto updateDefault(long actionPointId, EditActionPointDto editActionPointDto);
}
