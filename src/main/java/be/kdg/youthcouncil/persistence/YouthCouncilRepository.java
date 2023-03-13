package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouthCouncilRepository extends JpaRepository<YouthCouncil, Long> {
	YouthCouncil findByMunicipalityName(String municipalityName);
}
