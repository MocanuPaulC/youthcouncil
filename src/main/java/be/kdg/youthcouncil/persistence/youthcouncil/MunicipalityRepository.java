package be.kdg.youthcouncil.persistence.youthcouncil;

import be.kdg.youthcouncil.domain.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
	Optional<Municipality> findByName(String name);

}
