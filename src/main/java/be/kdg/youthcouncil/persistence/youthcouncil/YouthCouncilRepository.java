package be.kdg.youthcouncil.persistence.youthcouncil;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YouthCouncilRepository extends JpaRepository<YouthCouncil, Long> {

	@Query ("select yc from YouthCouncil yc where yc.municipality = (:municipality)")
	Optional<YouthCouncil> findByMunicipalityName(String municipality);

	@Query (
			"SELECT yc FROM YouthCouncil yc " +
					"JOIN FETCH yc.informativePages " +
					"WHERE yc.municipality = (:municipality)"
	)
	Optional<YouthCouncil> getWithInformativePages(String municipality);


	@Query (
			"SELECT yc from YouthCouncil yc " +
					"JOIN FETCH yc.actionPoints " +
					"WHERE yc.municipality = :municipality"
	)
	Optional<YouthCouncil> findWithActionPoints(String municipality);

	@Query (
			"SELECT yc from YouthCouncil yc " +
					"JOIN FETCH yc.actionPoints " +
					"WHERE yc.youthCouncilId = :youthCouncilId"
	)
	Optional<YouthCouncil> findWithActionPoints(long youthCouncilId);

	@Query (""" 
			SELECT yc FROM YouthCouncil yc
			JOIN FETCH yc.subscriptions
			WHERE yc.municipality = :municipality
			"""
	)
	Optional<YouthCouncil> findWithSubscriptions(String municipality);


	@Query (""" 
			SELECT yc FROM YouthCouncil yc
			JOIN FETCH yc.subscriptions
			WHERE yc.youthCouncilId = :youthCouncilId
			"""
	)
	Optional<YouthCouncil> findWithSubscriptions(long youthCouncilId);

	@Query (
			"SELECT s FROM YouthCouncilSubscription s " +
					"JOIN FETCH s.subscriber " +
					"WHERE s.youthCouncil.municipality = :municipality " +
					"AND s.role = 0"
	)
	Optional<YouthCouncil> findAdminsByMunicipality(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			JOIN FETCH yc.actionPoints
			WHERE yc.municipality = :municipality
			""")
	Optional<YouthCouncil> findWithActionPointsByMunicipality(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			JOIN FETCH yc.announcements
			WHERE yc.municipality = :municipality
			""")
	Optional<YouthCouncil> findWithAnnouncementsByMunicipality(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			JOIN FETCH yc.callForIdeas
			WHERE yc.municipality = :municipality
			""")
	Optional<YouthCouncil> findWithCallsForIdeasByMunicipality(String municipality);
}
