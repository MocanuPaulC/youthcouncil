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
	YouthCouncil findByMunicipalityName(String municipality);

	@Query (
			"SELECT yc FROM YouthCouncil yc " +
					"JOIN FETCH yc.informativePages " +
					"WHERE yc.municipality = (:municipality)"
	)
	Optional<YouthCouncil> getYouthCouncilWithInformativePages(@Param ("municipality") String municipality);
}
