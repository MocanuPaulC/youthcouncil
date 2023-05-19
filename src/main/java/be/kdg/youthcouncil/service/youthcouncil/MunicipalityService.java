package be.kdg.youthcouncil.service.youthcouncil;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.MunicipalityStatus;

import java.util.Map;
import java.util.Optional;

public interface MunicipalityService {
	Map<Long, MunicipalityStatus> getStatuses(Optional<PlatformUser> user);
}
