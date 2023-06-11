package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.config.security.BCryptConfig;
import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.config.security.CustomUserDetailsService;
import be.kdg.youthcouncil.controllers.mvc.viewModels.UserLogInViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.users.Authenticable;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.service.users.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
@AllArgsConstructor
@Controller
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	@GetMapping ("/login")
	public ModelAndView logIn(HttpServletRequest request, ModelAndView mav) {
		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("url_prior_login", referrer);
		mav.addObject("user", new UserLogInViewModel());
		mav.setViewName("login");
		return mav;
	}

	@GetMapping ("/register")
	public String register(Model model) {
		logger.debug("in getMapping of register");
		model.addAttribute("user", new UserRegisterViewModel());
		return "register";
	}

	@PostMapping ("/register")
	public String confirmRegister(@Valid @ModelAttribute ("user") UserRegisterViewModel viewModel, BindingResult errors, HttpServletRequest request) throws ServletException {
		logger.debug(viewModel.toString() + " in postMapping of register");
		logger.debug(errors.toString());
		if (errors.hasErrors()) {
			return "register";
		}
		String password = viewModel.getPassword();
		userService.create(viewModel);
		request.login(viewModel.getUsername(), password);
		return "redirect:/";
	}

	@GetMapping ("/users")
	public String users(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "users";
	}

	@GetMapping ("/profile")
	public String profile(Model model, Principal principal) {
		PlatformUser user = userService.findUserByUsername(principal.getName());
		model.addAttribute("user", user);
		return "profile";
	}

}
