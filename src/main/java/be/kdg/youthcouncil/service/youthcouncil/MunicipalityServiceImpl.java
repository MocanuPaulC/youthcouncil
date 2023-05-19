package be.kdg.youthcouncil.service.youthcouncil;

import be.kdg.youthcouncil.domain.Municipality;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.MunicipalityStatus;
import be.kdg.youthcouncil.persistence.youthcouncil.MunicipalityRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MunicipalityServiceImpl implements MunicipalityService {
	private final MunicipalityRepository municipalityJpaRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private YouthCouncilSubscriptionRepository subscriptionRepository;

	@Override
	public Map<Long, MunicipalityStatus> getStatuses(Optional<PlatformUser> user) {
		Map<Long, MunicipalityStatus> municipalityStatusMap = new HashMap<>();
		final List<Municipality> municipalities = youthCouncilRepository.findMunicipalitiesWithYouthcouncil();

		municipalityJpaRepository.findAll()
		                         .forEach(municipality ->
				                         municipalityStatusMap.put(municipality.getId(), MunicipalityStatus.YOUTHCOUNCIL_DOESNT_EXIST));
		municipalities.forEach(municipality -> municipalityStatusMap.put(municipality.getId(), MunicipalityStatus.YOUTHCOUNCIL_EXISTS));
		user.ifPresent(platformUser -> {
			List<Municipality> municipalitiesSubscribed = subscriptionRepository.findSubscribedMunicipalitiesByUser(user.get());
			municipalitiesSubscribed.forEach(municipality -> municipalityStatusMap.put(municipality.getId(), MunicipalityStatus.JOINED));
		});
		return municipalityStatusMap;
	}
}
