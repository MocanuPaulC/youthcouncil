package be.kdg.youthcouncil.persistence.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionPointReactionRepository extends JpaRepository<ActionPointReaction, Long> {

	Optional<ActionPointReaction> findByActionPointReactedOnAndReactingUser(ActionPoint actionPointReactedOn, PlatformUser reactingUser);

	long countByActionPointReactedOn(ActionPoint actionPoint);

	List<ActionPointReaction> findAllByActionPointReactedOnAndReactingUser(ActionPoint actionPoint, PlatformUser user);
}
