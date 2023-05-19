package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.config.security.CustomUserDetailsService;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/youthcouncils")
public class RestYouthCouncilController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final YouthCouncilService youthCouncilService;
	private final YouthCouncilSubscriptionService youthCouncilSubscriptionService;
	private final CallForIdeaService callForIdeaService;
	private final CustomUserDetailsService userDetailsService;

	private final UserService userService;

	@PostMapping ("/{youthCouncilId}/{userId}")
	public ResponseEntity<CallForIdeasDTO> joinCouncil(
			@PathVariable long youthCouncilId,
			@PathVariable long userId) {


		SecurityContext securityContext = SecurityContextHolder.getContext();

		//		logger.info("User {} is joining youth council {}", securityContext.getAuthentication().getPrincipal().getName(), youthCouncilId);
		youthCouncilSubscriptionService.create(youthCouncilId, userId);
		String youthCouncilMunicipality = youthCouncilService.getYouthCouncil(youthCouncilId)
		                                                     .getMunicipality();

		Authentication auth = securityContext.getAuthentication();

		List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
		updatedAuthorities.add(new SimpleGrantedAuthority("USER@" + youthCouncilMunicipality)); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

		securityContext.setAuthentication(newAuth);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping ("/{youthCouncilId}/{userId}")
	public ResponseEntity<CallForIdeasDTO> leaveCouncil(
			@PathVariable long youthCouncilId,
			@PathVariable long userId) {


		SecurityContext securityContext = SecurityContextHolder.getContext();

		//		logger.info("User {} is joining youth council {}", securityContext.getAuthentication().getPrincipal().getName(), youthCouncilId);
		youthCouncilSubscriptionService.remove(youthCouncilId, userId);
		String youthCouncilMunicipality = youthCouncilService.getYouthCouncil(youthCouncilId).getMunicipality();

		Authentication auth = securityContext.getAuthentication();

		List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
		updatedAuthorities.remove(new SimpleGrantedAuthority("USER@" + youthCouncilMunicipality)); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

		securityContext.setAuthentication(newAuth);

		return ResponseEntity.ok().build();
	}

	@PostMapping ("/{id}/callforideas")
	public ResponseEntity<CallForIdeasDTO> launchCallForIdeas(@PathVariable Long id, @Valid @RequestBody CallForIdeasDTO callForIdeasDTO, Principal principal) {
		logger.info("User {} is launching a call for ideas for youth council {}", principal.getName(), id);
		callForIdeaService.create(callForIdeasDTO);
		return ResponseEntity.ok(callForIdeasDTO);
	}
}
