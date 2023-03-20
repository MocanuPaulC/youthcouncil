package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.config.security.Oauth.CustomOAuth2User;
import be.kdg.youthcouncil.service.userService.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {
	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ModelAttribute
	public void addAttributes(Model model, Authentication authentication) {
		if (authentication == null) return;
		String username;
		if (authentication.getPrincipal() instanceof CustomOAuth2User) {
			username = ((CustomOAuth2User) authentication.getPrincipal()).getEmail();
		} else {
			username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
		}
		logger.debug("Authenticated user: " + username);
		final var user = userService.findByUsername(username);
		model.addAttribute("authUser", user);
	}
}
