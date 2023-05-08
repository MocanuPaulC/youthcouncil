package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;

import java.util.List;

public interface ActionPointService {

	EditActionPointDto update(long actionPointId, long youthCouncilId, EditActionPointDto editActionPointDto);

	EditActionPointDto updateDefault(long actionPointId, EditActionPointDto editActionPointDto);

	ActionPoint findById(long actionPointReactedOnId);

	void setDisplay(long actionPointId, boolean isDisplayed);

	List<ActionPointSubscription> findAllSubscriptionsByActionPointId(long actionPointId);

	void subscribe(long userId, long actionPointId);

	void unsubscribe(long userId, long actionPointId);
}
