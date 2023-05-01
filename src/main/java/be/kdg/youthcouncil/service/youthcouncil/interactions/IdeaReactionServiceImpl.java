package be.kdg.youthcouncil.service.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.exceptions.IdeaReactionNotFoundException;
import be.kdg.youthcouncil.exceptions.UserNotFoundException;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.interactions.IdeaReactionRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.IdeaRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IdeaReactionServiceImpl implements IdeaReactionService {

	private final IdeaReactionRepository ideaReactionRepository;
	private final IdeaRepository ideaRepository;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public IdeaReaction save(IdeaReaction reaction) {
		Optional<IdeaReaction> existingReaction = ideaReactionRepository.findByIdeaReactedOnAndReactingUser(reaction.getIdeaReactedOn(), reaction.getReactingUser());

		if (existingReaction.isPresent()) {
			if (existingReaction.get().getReaction().equals(reaction.getReaction())) {
				ideaReactionRepository.delete(existingReaction.get());
				return existingReaction.get();
			}
			reaction.setReactionId(existingReaction.get().getReactionId());
		}

		return ideaReactionRepository.save(reaction);

	}


	@Override
	public long getReactionCount(long entityId) {
		return ideaReactionRepository
				.countByIdeaReactedOn(ideaRepository
						.findById(entityId)
						.orElseThrow(IdeaReactionNotFoundException::new));
	}


	@Override
	public List<IdeaReaction> findAllUserReactionsToIdeas(List<Idea> ideas, PlatformUser user) {

		List<IdeaReaction> allReactions = new ArrayList<>();
		ideas.forEach(idea -> {
			allReactions.addAll(ideaReactionRepository.findAllByIdeaReactedOnAndReactingUser(idea, user));
		});
		return allReactions;
	}

	@Override
	public IdeaReaction findUserReactionToIdea(long ideaId, long userId) {
		Idea idea = ideaRepository.findById(ideaId)
		                          .orElseThrow(IdeaReactionNotFoundException::new);
		PlatformUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return ideaReactionRepository.findByIdeaReactedOnAndReactingUser(idea, user)
		                             .orElseThrow(IdeaReactionNotFoundException::new);

	}
}
