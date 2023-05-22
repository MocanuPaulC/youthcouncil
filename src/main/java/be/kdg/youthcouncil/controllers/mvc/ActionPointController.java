package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewActionPointViewModel;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class ActionPointController {


	private final ModelMapper modelMapper;
	private final YouthCouncilService youthCouncilService;
	private final ActionPointService actionPointService;
	private final ThemeRepository themeRepository;
	private final CallForIdeaService callForIdeaService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@GetMapping ("/youthcouncils/{municipality}/actionpoints/create")
	public String getActionPointPage(Model model, @PathVariable String municipality) {
		model.addAttribute("informativePage", new NewActionPointViewModel());
		model.addAttribute("municipality", municipality);
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("themes", themeRepository.findAll()
		                                            .stream()
		                                            .flatMap(theme -> theme.getSubThemes().stream())
		                                            .toArray());

		model.addAttribute("editorType", "create");
		model.addAttribute("actionPointType", "youthcouncil");
		return "actionPointEditor";
	}

	@GetMapping ("/youthcouncils/{municipality}/actionpoints/{id}/edit")
	public String editYouthCouncilInfoPage(@PathVariable String municipality, @PathVariable long id, Model model) {
		model.addAttribute("title", actionPointService.findById(id).getTitle());
		model.addAttribute("ownsubtheme", actionPointService.findById(id).getTheme().getSubTheme().toString());
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("actionPointId", id);
		model.addAttribute("themes", themeRepository.findAll()
		                                            .stream()
		                                            .flatMap(theme -> theme.getSubThemes().stream())
		                                            .toArray());
		model.addAttribute("editorType", "edit");
		model.addAttribute("actionPointType", "youthcouncil");
		return "actionPointEditor";
	}


	@GetMapping ("/youthcouncils/{municipality}/callforideas/{cfiID}/createactionpoint")
	public String getActionPointPageFromCallForIdeas(Model model, @PathVariable String municipality, @PathVariable long cfiID) {
		CallForIdea cfi = callForIdeaService.findWithIdeas(cfiID);
		model.addAttribute("informativePage", new NewActionPointViewModel());
		model.addAttribute("municipality", municipality);
		model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality));
		model.addAttribute("themes", cfi.getTheme().getSubThemes());
		model.addAttribute("ideas", cfi.getIdeas().toArray());
		model.addAttribute("callForIdeasId", cfiID);
		model.addAttribute("editorType", "create");
		model.addAttribute("actionPointType", "callforideas");
		return "actionPointEditor";
	}


}
