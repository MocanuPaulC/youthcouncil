package be.kdg.youthcouncil.persistence.youthcouncil;

import java.util.Map;
import java.util.Optional;

public interface MunicipalityRepository {

	Map<Integer, String> findAll();

	Optional<String> findByNIS(int nis);

	Optional<Integer> findByName(String name);
}
