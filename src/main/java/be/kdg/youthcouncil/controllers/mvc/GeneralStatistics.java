package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
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
	private final YouthCouncilSubscriptionService youthCouncilSubscriptionService;
	private final UserService userService;

	@GetMapping
	public String youthCouncilStatistics(Model model) {
		model.addAttribute("subscriptions", youthCouncilSubscriptionService.findAllYouthCouncilSubscriptionsWithUniqueUsers());

		return "statistics";
	}

}
