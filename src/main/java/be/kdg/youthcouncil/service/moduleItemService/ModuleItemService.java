package be.kdg.youthcouncil.service.moduleItemService;

import be.kdg.youthcouncil.controllers.api.dto.EditActionPointDto;
import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;

import java.util.List;

public interface ModuleItemService {
	List<ModuleItem> getAllModuleItems();

	void handleCallForIdeas(String idea, long moduleItemId, long userId, String theme);

	EditActionPointDto updateActionPoint(long oldActionPointId, long youthCouncilId, EditActionPointDto editActionPointDto);
}
