package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.config.security.Oauth.CustomOAuth2User;
import be.kdg.youthcouncil.domain.users.GeneralAdmin;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.exceptions.InformativePageNotFoundException;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.exceptions.UserNotFoundException;
import be.kdg.youthcouncil.service.users.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {
	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ModelAttribute
	public void addAttributes(Model model, Authentication authentication) {
		logger.debug("user is authenicating.......");
		if (authentication == null) return;
		String username;
		if (authentication.getPrincipal() instanceof CustomOAuth2User) {
			username = ((CustomOAuth2User) authentication.getPrincipal()).getEmail();
		} else {
			username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
		}
		logger.debug("Authenticated user: " + username);

		PlatformUser user = null;
		GeneralAdmin admin = null;

		if (!userService.checkIfAuthenticableExists(username)) {
			throw new UsernameNotFoundException("The username for the logged in user could not be found!");
		}

		if (userService.checkIfUserExists(username)) {
			user = userService.findWithSubscriptionsAndYouthCouncils(username);
		} else if (userService.checkIfAdminExists(username)) {
			admin = userService.findAdminByUsername(username);
		}

		model.addAttribute("authUser", user);
		model.addAttribute("authAdmin", admin);
		if (user != null) {
			model.addAttribute("notifications", userService.findLatest10AllNotifications(user.getId()));
		}
	}

	@ExceptionHandler (InformativePageNotFoundException.class)
	public String handleInformativePageNotFoundException(InformativePageNotFoundException e, Model model) {
		model.addAttribute("error", e.getMessage());
		return "error";
	}

	@ExceptionHandler (MunicipalityNotFoundException.class)
	public String handleMunicipalityNotFoundException(MunicipalityNotFoundException e, Model model) {
		model.addAttribute("error", e.getMessage());
		return "error";
	}

	@ExceptionHandler (UserNotFoundException.class)
	public String handleUserNotFoundException(UserNotFoundException e, Model model) {
		model.addAttribute("error", e.getMessage());
		return "error";
	}
}
