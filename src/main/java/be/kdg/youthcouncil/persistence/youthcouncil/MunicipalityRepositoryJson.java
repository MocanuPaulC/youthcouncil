package be.kdg.youthcouncil.persistence.youthcouncil;

import be.kdg.youthcouncil.exceptions.MunicipalitiesJsonNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MunicipalityRepositoryJson implements MunicipalityRepository {

	private final HashMap<Integer, String> municipalities;

	public MunicipalityRepositoryJson() {
		String filename = "src/main/resources/static/municipalities.json";


		try {
			Type listType = new TypeToken<HashMap<Integer, String>>() {}.getType();
			Gson gson = new Gson();
			municipalities = gson.fromJson(new FileReader(filename), listType);

		} catch (FileNotFoundException e) {
			throw new MunicipalitiesJsonNotFoundException();
		}
	}


	@Override
	public Map<Integer, String> findAll() {
		Map<Integer, String> clone = new HashMap<>();
		clone.putAll(municipalities);
		return clone;
	}

	@Override
	public Optional<String> findByNIS(int nis) {
		if (!municipalities.containsKey(nis)) {
			return Optional.empty();
		}
		return Optional.of(municipalities.get(nis));
	}

	@Override
	public Optional<Integer> findByName(String name) {
		if (!municipalities.containsValue(name)) {
			return Optional.empty();
		}

		for (Integer key : municipalities.keySet()) {
			if (municipalities.get(key).equals(name)) {
				return Optional.of(key);
			}
		}
		return Optional.empty();
	}
}
