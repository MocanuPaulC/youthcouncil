package be.kdg.youthcouncil.persistence.youthcouncil;

import be.kdg.youthcouncil.domain.Municipality;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YouthCouncilRepository extends JpaRepository<YouthCouncil, Long> {

	@Query ("select yc from YouthCouncil yc where yc.municipality.name = (:municipality)")
	Optional<YouthCouncil> findByMunicipalityName(String municipality);

	@Query (
			"SELECT yc FROM YouthCouncil yc " +
					"JOIN FETCH yc.informativePages " +
					"WHERE yc.municipality.name = (:municipality)"
	)
	Optional<YouthCouncil> getWithInformativePages(String municipality);


	@Query (
			"SELECT yc from YouthCouncil yc " +
					"JOIN FETCH yc.actionPoints " +
					"WHERE yc.municipality.name = :municipality"
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
			WHERE yc.municipality.name = :municipality
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
					"WHERE s.youthCouncil.municipality.name = :municipality " +
					"AND s.role = 0"
	)
	Optional<YouthCouncil> findAdminsByMunicipality(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			JOIN FETCH yc.actionPoints ap
			WHERE yc.municipality.name = :municipality
			AND ap.moduleStatus = 0
			""")
	Optional<YouthCouncil> findByMunicipalityWithActionPointsToDisplay(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			LEFT JOIN FETCH yc.announcements
			WHERE yc.municipality.name = :municipality
			""")
	Optional<YouthCouncil> findWithAnnouncementsByMunicipality(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			LEFT JOIN FETCH yc.callForIdeas
			WHERE yc.municipality.name = :municipality
			""")
	Optional<YouthCouncil> findWithCallsForIdeasByMunicipality(String municipality);


	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			LEFT JOIN FETCH yc.actionPoints
			WHERE yc.municipality.name = :municipality
			""")
	Optional<YouthCouncil> findByMunicipalityWithActionPoints(String municipality);

	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			LEFT JOIN FETCH yc.callForIdeas cfi
			WHERE yc.municipality.name = :municipality
			AND cfi.moduleStatus = 0
			""")
	Optional<YouthCouncil> findByMunicipalityWithCallForIdeasDisplayed(String municipality);


	@Query ("""
			SELECT  yc FROM YouthCouncil yc
			LEFT JOIN FETCH yc.announcements an
			WHERE yc.municipality.name = :municipality
			AND an.moduleStatus = 0
			""")
	Optional<YouthCouncil> findByMunicipalityWithAnnouncementsDisplayed(String municipality);

	@Query ("""
			SELECT yc.municipality FROM YouthCouncil yc
			""")
	List<Municipality> findMunicipalitiesWithYouthcouncil();
}
