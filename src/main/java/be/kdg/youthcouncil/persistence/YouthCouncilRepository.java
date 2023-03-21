package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	Optional<YouthCouncil> getYouthCouncilWithInformativePages(@Param ("municipality") String municipality);

	@Query (
			"SELECT y FROM YouthCouncil y " +
					"JOIN FETCH y.councilMembers " +
					"WHERE y.municipality = (:municipality)"
	)
	Optional<YouthCouncil> findByMunicipalityNameWithCouncilMembers(@Param ("municipality") String municipality);

	@Query (
			"SELECT y FROM YouthCouncil y " +
					"JOIN FETCH y.councilMembers " +
					"WHERE y.id = (:youthCouncilId)"
	)
	Optional<YouthCouncil> findByIdWithCouncilMembers(@Param ("youthCouncilId") long youthCouncilId);

	@Query (
			"SELECT y FROM YouthCouncil y " +
					"JOIN FETCH y.councilAdmins " +
					"WHERE y.municipality = (:municipality)"
	)
	Optional<YouthCouncil> findByMunicipalityNameWithCouncilAdmins(@Param ("municipality") String municipality);

}
