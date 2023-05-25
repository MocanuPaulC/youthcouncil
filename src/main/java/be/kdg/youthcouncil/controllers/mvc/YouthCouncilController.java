package be.kdg.youthcouncil.controllers.mvc;


import be.kdg.youthcouncil.config.security.BCryptConfig;
import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.controllers.mvc.viewModels.CouncilAdminViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.NewAnnoucementViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.ActionPointStatus;
import be.kdg.youthcouncil.service.EmailService;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.interactions.ActionPointReactionService;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import be.kdg.youthcouncil.service.youthcouncil.modules.AnnouncementService;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import be.kdg.youthcouncil.service.youthcouncil.modules.InformativePageService;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping ("/youthcouncils")
public class YouthCouncilController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ModelMapper modelMapper;

	private final YouthCouncilService youthCouncilService;
	private final YouthCouncilSubscriptionService youthCouncilSubscriptionService;
	private final ActionPointReactionService actionPointReactionService;
	private final ActionPointService actionPointService;
	private final UserService userService;
	private final EmailService emailService;
	private final BCryptConfig bCryptConfig;
	private final CallForIdeaService callForIdeaService;
	private final InformativePageService informativePageService;

	private final AnnouncementService announcementService;

	@GetMapping ()
	public String youthCouncils(Model model) {
		model.addAttribute("councils", youthCouncilService.findAllYouthCouncils());
		return "youthCouncils";
	}

	@GetMapping ("/add")
	public String getAddYouthCouncil(Model model) {
		model.addAttribute("youthCouncil", new NewYouthCouncilViewModel());
		return "addYouthCouncil";
	}

	@PostMapping ("/add")
	public String addYouthCouncil(@Valid @ModelAttribute ("youthCouncil") NewYouthCouncilViewModel viewModel, BindingResult errors, HttpServletRequest request) {
		logger.debug(viewModel.toString() + " in postMapping of addYouthCouncil");
		if (errors.hasErrors()) {
			return "addYouthCouncil";
		}
		youthCouncilService.create(viewModel);
		return "redirect:/youthcouncils";
	}

	@GetMapping ("/{municipality}")
	public String youthCouncil(Model model, @PathVariable String municipality, Principal principal) {
		PlatformUser user = null;
		if (principal != null) {
			user = userService.findUserByUsername(principal.getName());
		}

		model.addAttribute("themes", callForIdeaService.findAllThemes());
		model.addAttribute("ycWithAnnouncements", youthCouncilService.findByMunicipalityWithAnnouncementsDisplayed(municipality));
		model.addAttribute("ycWithCallsForIdeas", youthCouncilService.findByMunicipalityWithCallsForIdeasDisplayed(municipality));
		YouthCouncil ycWithActionPoints = youthCouncilService.findByMunicipalityWithActionPointsDisplayed(municipality);
		model.addAttribute("ycWithActionPoints", ycWithActionPoints);
		model.addAttribute("userReactions", actionPointReactionService.findAllUserReactionsToActionPoints(ycWithActionPoints.getActionPoints(), user));
		model.addAttribute("ycInfoPages", informativePageService.findAllByMunicipalityName(municipality));

		return "youthCouncil";
	}

	@GetMapping ("/{municipality}/edit")
	public String editYouthCouncil(Model model, @PathVariable String municipality, Principal principal) {
		PlatformUser user = null;
		if (principal != null) {
			user = userService.findUserByUsername(principal.getName());
		}
		model.addAttribute("ycWithAnnouncements", youthCouncilService.findByMunicipalityWithAnnouncements(municipality));
		model.addAttribute("ycWithCallsForIdeas", youthCouncilService.findByMunicipalityWithCallsForIdeas(municipality));
		YouthCouncil ycWithActionPoints = youthCouncilService.findByMunicipalityWithActionPoints(municipality);
		model.addAttribute("ycWithActionPoints", ycWithActionPoints);
		model.addAttribute("userReactions", actionPointReactionService.findAllUserReactionsToActionPoints(ycWithActionPoints.getActionPoints(), user));


		return "editYouthCouncil";
	}

	@GetMapping ("/{municipality}/user-management")
	public String youthCouncilUserManagement(Model model, @PathVariable String municipality) {
		YouthCouncil possibleYouthCouncil = youthCouncilService.findByMunicipality(municipality);
		model.addAttribute("youthCouncil", possibleYouthCouncil);
		model.addAttribute("subscriptions", youthCouncilSubscriptionService.findAllByYouthCouncil(possibleYouthCouncil));


		return "userManagement";
	}

	@GetMapping ("/{municipality}/callforideas")
	public String callForIdeas(Model model, @PathVariable String municipality) {
		List<CallForIdea> callForIdeas = callForIdeaService.findAllByMunicipalityNameWithIdeas(municipality);

		model.addAttribute("callForIdeas", callForIdeas);
		return "callForIdeas";
	}

	@GetMapping ("/{municipality}/callforideas/{callForIdeaId}")
	public String youthCouncilCallForAction(Model model, @PathVariable String municipality, @PathVariable long callForIdeaId) {
		CallForIdea callForIdea = callForIdeaService.findByIdWithIdeasWithReactions(callForIdeaId);

		model.addAttribute("callForIdea", callForIdea);
		return "callForIdea";
	}


	@GetMapping ("/{municipality}/create-council-admin")
	public String getCreateCouncilAdmin(@PathVariable String municipality, Model model) {

		model.addAttribute("council", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("councilAdmin", new CouncilAdminViewModel());
		return "createCouncilAdmin";
	}

	@PostMapping ("/{municipality}/create-council-admin")
	public String createCouncilAdmin(
			@PathVariable String municipality,
			Model model,
			@Valid @ModelAttribute ("councilAdmin") CouncilAdminViewModel viewModel,
			BindingResult errors,
			HttpServletRequest request) {

		if (errors.hasErrors()) {

			return "createCouncilAdmin";
		} else {
			UserRegisterViewModel userRegisterViewModel = new UserRegisterViewModel();
			userRegisterViewModel.setEmail(viewModel.getEmail());
			userRegisterViewModel.setUsername(viewModel.getEmail());
			userRegisterViewModel.setPassword(bCryptConfig.passwordEncoder().encode(viewModel.getPassword()));
			userService.save(userRegisterViewModel);
			emailService.sendSimpleMessage(viewModel.getEmail(), "Youth Council Admin", "You have been added as an admin to the youth council of " + municipality + ".\n" +
					"Your username is: " + viewModel.getEmail() + "\n" +
					"Your password is: " + viewModel.getPassword() + "\n" +
					"Please change your password after logging in.");
		}

		return "redirect:/";
	}


	@GetMapping ("/{municipality}/actionpoints/{actionpointid}")
	public String getActionPointsOfYouthCouncil(@PathVariable String municipality, @PathVariable long actionpointid, Model model, Authentication authentication) {
		//TODO: change this to get the actionpoint by id from the actionPointService directly

		ActionPoint actionPoint = actionPointService.findByIdWithIdeas(actionpointid);
		model.addAttribute("actionPoint", actionPoint);
		model.addAttribute("labels", ActionPointStatus.values());


		YouthCouncil youthCouncil = youthCouncilService.findByMunicipalityWithActionPointsDisplayed(municipality);
		model.addAttribute("youthCouncil", youthCouncil);

		if (authentication != null) {
			model.addAttribute("userHasSubscription", userService.hasSubscriptionToActionPoint(((CustomUserDetails) authentication.getPrincipal()).getUserId(), actionpointid));
		} else {
			model.addAttribute("userHasSubscription", false);
		}
		if (actionPoint.getInspiredBy().size() > 0) {
			model.addAttribute("ideas", actionPoint.getInspiredBy());
		}
		return "newActionPoint";


	}


	@GetMapping ("/{municipality}/announcements")
	public String getAnnouncements(Model model, @PathVariable String municipality) {
		model.addAttribute("municipality", municipality);
		model.addAttribute("announcements", announcementService.findAll(municipality));
		return "announcements";
	}

	@GetMapping ("/{municipality}/announcements/{annoucementsid}")
	public String getAnnouncements(Model model, @PathVariable String municipality, @PathVariable long annoucementsid) {
		model.addAttribute("municipality", municipality);
		model.addAttribute("announcement", announcementService.findByIdAndMunicipality(municipality, annoucementsid));
		return "announcement";
	}

	@GetMapping ("/{municipality}/announcements/add")
	public String getAddAnnouncements(Model model, @PathVariable String municipality) {
		model.addAttribute("announcement", new NewAnnoucementViewModel());
		model.addAttribute("municipality", municipality);
		return "addAnnouncement";
	}

	@PostMapping ("/{municipality}/announcements/add")
	public String addAnnouncement(@PathVariable String municipality, @Valid @ModelAttribute ("announcement") NewAnnoucementViewModel viewModel, BindingResult errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("municipality", municipality);
			model.addAttribute("announcement", viewModel);
			return "addAnnouncement";
		}
		announcementService.save(municipality, viewModel);
		return "redirect:/youthcouncils/" + municipality;
	}


}
