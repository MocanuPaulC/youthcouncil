package be.kdg.youthcouncil.service.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdeaReactionService {

	IdeaReaction save(IdeaReaction reaction);

	long getReactionCount(long entityId);

	List<IdeaReaction> findAllUserReactionsToIdeas(List<Idea> actionPoints, PlatformUser user);

	IdeaReaction findUserReactionToIdea(long ideaId, long userId);
}
