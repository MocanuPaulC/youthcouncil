package be.kdg.youthcouncil.persistence.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaReactionRepository extends JpaRepository<IdeaReaction, Long> {

	Optional<IdeaReaction> findByIdeaReactedOnAndReactingUser(Idea ideaReactedOn, PlatformUser reactingUser);

	long countByIdeaReactedOn(Idea actionPoint);

	List<IdeaReaction> findAllByIdeaReactedOnAndReactingUser(Idea idea, PlatformUser user);
}
