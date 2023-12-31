package be.kdg.youthcouncil.service.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActionPointReactionService {

	ActionPointReaction save(ActionPointReaction reaction);

	long getReactionCount(long entityId);

	List<ActionPointReaction> findAllUserReactionsToActionPoints(List<ActionPoint> actionPoints, PlatformUser user);

	ActionPointReaction findUserReactionToActionPoint(long actionPointId, long userId);
}
