package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.domain.youthcouncil.MunicipalityStatus;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.MunicipalityService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping ("/api/municipalities/")
@RestController ()
public class RestMunicipalityController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MunicipalityService municipalityService;
	private final UserService userService;

	@GetMapping
	public ResponseEntity<Map<Long, MunicipalityStatus>> getMunicipalities(Principal principal) {
		if (principal == null) {
			return ResponseEntity.ok(municipalityService.getStatuses(Optional.empty()));
		}

		return ResponseEntity.ok(municipalityService.getStatuses(Optional.of(userService.findUserByUsername(principal.getName()))));
	}
}
