package be.kdg.youthcouncil.service.youthcouncil.interactions;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions.ActionPointReactionDto;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.exceptions.ActionPointReactionNotFound;
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

@Service
@AllArgsConstructor
public class ActionPointReactionServiceImpl implements ActionPointReactionService {


	private final ActionPointReactionRepository actionPointReactionRepository;
	private final ActionPointRepository actionPointRepository;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ActionPointReaction save(ActionPointReaction reaction) {
		ActionPointReaction existingReaction = actionPointReactionRepository.findByActionPointReactedOnAndReactingUser(reaction.getActionPointReactedOn(), reaction.getReactingUser())
		                                                                    .orElse(null);
		// null means we make a new reaction
		if (existingReaction == null) {
			return actionPointReactionRepository.save(reaction);
		}
		// if it's the same reaction, we remove it
		if (existingReaction.getReaction().equals(reaction.getReaction())) {
			actionPointReactionRepository.delete(existingReaction);
			return existingReaction;
		}
		// different reaction means we change it
		existingReaction.setReaction(reaction.getReaction());
		return actionPointReactionRepository.save(existingReaction);
	}


	@Override
	public void addReactionCount(ActionPointReactionDto returnDto) {
		returnDto
				.setReactionCount(actionPointReactionRepository
						.countByActionPointReactedOn(actionPointRepository
								.findById(returnDto.getActionPointReactedOnId())
								.orElseThrow(ActionPointReactionNotFound::new)));
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
		                                               .orElseThrow(ActionPointReactionNotFound::new);
		PlatformUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return actionPointReactionRepository.findByActionPointReactedOnAndReactingUser(actionPoint, user)
		                                    .orElseThrow(ActionPointReactionNotFound::new);

	}
}
