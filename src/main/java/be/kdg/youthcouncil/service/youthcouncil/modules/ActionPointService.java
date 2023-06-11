package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.ActionPointDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.IdeaIdDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;

import java.util.List;
import java.util.Optional;

public interface ActionPointService {

	EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto);

	ActionPoint save(String title, boolean isDefault, List<BlockDto> infoPageBlocksDto, Optional<String> municipality, String theme);

	boolean existsByTitle(Optional<String> municipality, String title);

	List<ActionPoint> findAllByMunicipality(String municipality);

	ActionPoint findById(long actionPointReactedOnId);

	void setDisplay(long actionPointId, boolean isDisplayed);

	List<ActionPointSubscription> findAllSubscriptionsByActionPointId(long actionPointId);

	void subscribe(long userId, long actionPointId);

	void unsubscribe(long userId, long actionPointId);

	List<BlockDto> findActionPointBlocks(Optional<String> municipality, long actionPointId);

	ActionPointDto linkIdeaToActionPoint(String title, long cfiId, long youthCouncilID, List<IdeaIdDto> ideaIdDtoList);

	ActionPoint findByIdWithIdeas(long l);
}
