package be.kdg.youthcouncil.service.youthcouncil;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.MunicipalityStatus;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.MunicipalityRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MunicipalityServiceImpl implements MunicipalityService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final MunicipalityRepository municipalityRepository;
	private final YouthCouncilSubscriptionRepository subscriptionRepository;
	private final YouthCouncilRepository youthCouncilRepository;

	@Override
	public Map<Integer, String> findAll() {
		return municipalityRepository.findAll();
	}

	@Override
	public String getNameFromNIS(int nis) {
		return municipalityRepository.findByNIS(nis)
		                             .orElseThrow(() -> new MunicipalityNotFoundException(String.valueOf(nis)));
	}

	@Override
	public int getNISFromName(String name) {
		return municipalityRepository.findByName(name).orElseThrow(() -> new MunicipalityNotFoundException(name));
	}

	/*
	 * This Method returns a map of each NIS code with a status relative to that user.
	 * The status defines whether the user has joined that youthcouncil or that youthcouncil
	 * exists for the municipality or not.
	 */
	@Override
	public Map<Integer, MunicipalityStatus> getStatuses(Optional<PlatformUser> user) {
		Map<Integer, String> municipalities = municipalityRepository.findAll();
		List<String> municipalitiesWithYouthcouncil = youthCouncilRepository.findMunicipalitiesWithYouthcouncil();


		Map<Integer, MunicipalityStatus> out = new HashMap<>();
		for (Integer key : municipalities.keySet()) {
			out.put(key, MunicipalityStatus.YOUTHCOUNCIL_DOESNT_EXIST);
		}
		for (Integer key : municipalities.entrySet().stream()
		                                 .filter(entry -> municipalitiesWithYouthcouncil.contains(entry.getValue()))
		                                 .map(Map.Entry::getKey).toList()) {
			out.put(key, MunicipalityStatus.YOUTHCOUNCIL_EXISTS);
		}
		if (user.isPresent()) {
			List<String> municipalitiesSubscribed = subscriptionRepository.findSubscribedMunicipalitiesByUser(user.get());
			for (Integer key : municipalities.entrySet().stream()
			                                 .filter(entry -> municipalitiesSubscribed.contains(entry.getValue()))
			                                 .map(Map.Entry::getKey).toList()) {
				out.put(key, MunicipalityStatus.JOINED);
			}
		}
		return out;
	}
}
