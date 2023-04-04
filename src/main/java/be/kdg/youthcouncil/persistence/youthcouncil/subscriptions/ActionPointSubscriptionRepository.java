package be.kdg.youthcouncil.persistence.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionPointSubscriptionRepository extends JpaRepository<ActionPointSubscription, Long> {
}
