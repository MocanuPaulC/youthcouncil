package be.kdg.youthcouncil.persistence.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionPointSubscriptionRepository extends JpaRepository<ActionPointSubscription, Long> {

	Optional<ActionPointSubscription> findByActionPointAndSubscriber(ActionPoint actionPointId, PlatformUser subscriber);
}
