package be.kdg.youthcouncil.persistence.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YouthCouncilSubscriptionRepository extends JpaRepository<YouthCouncilSubscription, Long> {

	@Query (
			"SELECT s FROM YouthCouncilSubscription s " +
					"JOIN FETCH s.youthCouncil " +
					"WHERE s.youthCouncilSubscriptionId = :subscriptionId"
	)
	Optional<YouthCouncilSubscription> findWithYouthCouncil(long subscriptionId);

	@Query (
			"SELECT s.subscriber FROM YouthCouncilSubscription s " +
					"WHERE s.youthCouncil.municipality = :municipality"
	)
	List<PlatformUser> findSubscribersByMunicipality(String municipality);


	@Query (
			"SELECT s FROM YouthCouncilSubscription s " +
					"JOIN FETCH s.youthCouncil " +
					"WHERE s.youthCouncil.youthCouncilId = :youthCouncilId"
	)
	List<YouthCouncilSubscription> findByYouthCouncil(long youthCouncilId);

	@Query (
			"SELECT s FROM YouthCouncilSubscription s " +
					"WHERE s.youthCouncil= :youthCouncil"
	)
	List<YouthCouncilSubscription> findAllByYouthCouncil(YouthCouncil youthCouncil);

	@Query ("""
			         SELECT s.youthCouncil.municipality 
			         FROM YouthCouncilSubscription s 
			         WHERE s.subscriber = :user
			""")
	List<String> findSubscribedMunicipalitiesByUser(PlatformUser user);

	Optional<YouthCouncilSubscription> findBySubscriberAndYouthCouncil(PlatformUser subscriber, YouthCouncil youthCouncil);

	Optional<YouthCouncilSubscription> findBySubscriber_userIdAndYouthCouncil_YouthCouncilId(long userId, long youthCouncilId);


	Optional<YouthCouncilSubscription> findBySubscriber_userIdAndYouthCouncil_Municipality(long userId, String s);
}


