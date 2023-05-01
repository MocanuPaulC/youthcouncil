package be.kdg.youthcouncil.service.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.exceptions.ActionPointReactionNotFoundException;
import be.kdg.youthcouncil.exceptions.UserNotFoundException;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.interactions.ActionPointReactionRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActionPointReactionServiceImpl implements ActionPointReactionService {


	private final ActionPointReactionRepository actionPointReactionRepository;
	private final ActionPointRepository actionPointRepository;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ActionPointReaction save(ActionPointReaction reaction) {
		Optional<ActionPointReaction> existingReaction = actionPointReactionRepository.findByActionPointReactedOnAndReactingUser(reaction.getActionPointReactedOn(), reaction.getReactingUser());

		if (existingReaction.isPresent()) {
			if (existingReaction.get().getReaction().equals(reaction.getReaction())) {
				actionPointReactionRepository.delete(existingReaction.get());
				return existingReaction.get();
			}
			reaction.setReactionId(existingReaction.get().getReactionId());
		}

		return actionPointReactionRepository.save(reaction);

	}


	@Override
	public long getReactionCount(long entityId) {
		return actionPointReactionRepository
				.countByActionPointReactedOn(actionPointRepository
						.findById(entityId)
						.orElseThrow(ActionPointReactionNotFoundException::new));
	}


	@Override
	public List<ActionPointReaction> findAllUserReactionsToActionPoints(List<ActionPoint> actionPoints, PlatformUser user) {

		List<ActionPointReaction> allReactions = new ArrayList<>();
		actionPoints.forEach(actionPoint -> {
			allReactions.addAll(actionPointReactionRepository.findAllByActionPointReactedOnAndReactingUser(actionPoint, user));
		});
		return allReactions;
	}

	@Override
	public ActionPointReaction findUserReactionToActionPoint(long actionPointId, long userId) {
		ActionPoint actionPoint = actionPointRepository.findById(actionPointId)
		                                               .orElseThrow(ActionPointReactionNotFoundException::new);
		PlatformUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return actionPointReactionRepository.findByActionPointReactedOnAndReactingUser(actionPoint, user)
		                                    .orElseThrow(ActionPointReactionNotFoundException::new);

	}
}
