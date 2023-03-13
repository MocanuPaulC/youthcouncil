package be.kdg.youthcouncil.controllers.mvc;


import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.service.youthCouncilService.YouthCouncilService;
import be.kdg.youthcouncil.config.security.annotations.GAOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/youthcouncils")
public class YouthCouncilController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final YouthCouncilService youthCouncilService;

    public YouthCouncilController(YouthCouncilService youthCouncilService) {
        this.youthCouncilService = youthCouncilService;
    }

    @GetMapping()
    public String youthCouncils(Model model) {
        model.addAttribute("councils", youthCouncilService.getAllYouthCouncils());
        return "youthCouncils";
    }

    @GAOnly
    @GetMapping("/add")
    public String getAddYouthCouncil(Model model) {
        model.addAttribute("youthCouncil", new NewYouthCouncilViewModel());
        return "addYouthCouncil";
    }

    @GAOnly
    @PostMapping("/add")
    public String addYouthCouncil(@ModelAttribute("youthCouncil") NewYouthCouncilViewModel viewModel, HttpServletRequest request, BindingResult errors) {
        logger.debug(viewModel.toString() + " in postMapping of addYouthCouncil");
        if(errors.hasErrors()) {
            return "addYouthCouncil";
        }
        youthCouncilService.create(viewModel);
        return "redirect:/";
    }
}
