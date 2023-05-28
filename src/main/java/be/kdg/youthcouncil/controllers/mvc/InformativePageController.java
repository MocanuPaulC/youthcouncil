package be.kdg.youthcouncil.controllers.mvc;


import be.kdg.youthcouncil.controllers.mvc.viewModels.NewInformativePageViewModel;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.exceptions.InformativePageNotFoundException;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.modules.InformativePageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class InformativePageController {

	private final ModelMapper modelMapper;
	private final YouthCouncilService youthCouncilService;
	private final InformativePageService informativePageService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@GetMapping ("/youthcouncils/{municipality}/informativepages")
	public String informativePages(Model model, @PathVariable String municipality) {
		List<InformativePage> pages = youthCouncilService.getAllInformativePages(municipality);
		model.addAttribute("municipality", municipality);
		model.addAttribute("informativePages", pages);
		return "informativePages";
	}

	@GetMapping ("/youthcouncils/{municipality}/informativepages/create")
	public String getAddInformativePage(Model model, @PathVariable String municipality) {
		model.addAttribute("informativePage", new NewInformativePageViewModel());
		model.addAttribute("municipality", municipality);
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("editorType", "create");
		model.addAttribute("infoPageType", "youthcouncil");
		return "informativePageEditor";
	}


	@GetMapping ("/youthcouncils/{municipality}/informativepages/{title}")
	public String getYouthCouncilInfoPage(@PathVariable String municipality, @PathVariable String title, Model model) {
		InformativePage infopage = informativePageService.findInfoPage(title, Optional.of(municipality));
		model.addAttribute("informativePage", infopage);
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		return "informativePage";
	}

	@GetMapping ("/youthcouncils/{municipality}/informativepages/{title}/edit")
	public String editYouthCouncilInfoPage(@PathVariable String municipality, @PathVariable String title, Model model) {
		if (!informativePageService.exists(Optional.of(municipality), title))
			throw new InformativePageNotFoundException(title, municipality);
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("editorType", "edit");
		model.addAttribute("infoPageType", "youthcouncil");
		return "informativePageEditor";
	}


	@GetMapping ("/informativepages/create")
	public String createDefaultInfoPage(Model model) {
		model.addAttribute("editorType", "create");
		model.addAttribute("infoPageType", "default");
		return "informativePageEditor";
	}


	@GetMapping ("/informativepages/{title}")
	public String getDefaultInfoPage(@PathVariable String title, Model model) {
		InformativePage infopage = informativePageService.findDefaultInfoPage(title);
		model.addAttribute("informativePage", infopage);
		return "informativePage";
	}

	@GetMapping ("/informativepages/{title}/edit")
	public String editYouthCouncilInfoPage(@PathVariable String title, Model model) {
		logger.debug(informativePageService.findInfoPage(title, Optional.empty()).toString());
		if (!informativePageService.exists(Optional.empty(), title)) {
			throw new InformativePageNotFoundException(title);
		}
		model.addAttribute("editorType", "edit");
		model.addAttribute("infoPageType", "default");
		return "informativePageEditor";
	}

}
