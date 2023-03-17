package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.config.security.annotations.GAOnly;
import be.kdg.youthcouncil.service.userService.UserService;
import be.kdg.youthcouncil.service.youthCouncilService.YouthCouncilService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/statistics")
@AllArgsConstructor
public class GeneralStatistics {

	private final YouthCouncilService youthCouncilService;
	private final UserService userService;

	@GAOnly
	@GetMapping
	public String youthCouncilStatistics(Model model) {
		model.addAttribute("users", userService.findAllUsers());

		return "statistics";
	}

}

