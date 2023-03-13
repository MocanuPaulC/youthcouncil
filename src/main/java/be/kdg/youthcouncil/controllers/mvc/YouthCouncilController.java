package be.kdg.youthcouncil.controllers.mvc;


import be.kdg.youthcouncil.config.security.annotations.GAOnly;
import be.kdg.youthcouncil.controllers.mvc.viewModels.CouncilAdminViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.service.userService.UserService;
import be.kdg.youthcouncil.service.youthCouncilService.YouthCouncilService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/youthcouncils")
public class YouthCouncilController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ModelMapper modelMapper;

    private final YouthCouncilService youthCouncilService;
    private final UserService userService;

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
    public String addYouthCouncil(@Valid @ModelAttribute("youthCouncil") NewYouthCouncilViewModel viewModel, BindingResult errors, HttpServletRequest request) {
        logger.debug(viewModel.toString() + " in postMapping of addYouthCouncil");
        if (errors.hasErrors()) {
            return "addYouthCouncil";
        }
        youthCouncilService.create(viewModel);
        return "redirect:/";
    }

    @GetMapping("/{municipality}")
    public String youthCouncil(Model model, @PathVariable String municipality) {
        model.addAttribute("youthCouncil", youthCouncilService.findByMunicipality(municipality).orElse(null));
        return "youthCouncil";
    }

    @GetMapping("/{municipality}/create-council-admin")
    public String getCreateCouncilAdmin(@PathVariable String municipality, Model model) {

        model.addAttribute("council", youthCouncilService.findByMunicipality(municipality).orElse(null));
        model.addAttribute("councilAdmin", new CouncilAdminViewModel());
        return "createCouncilAdmin";
    }

    @PostMapping("/{municipality}/create-council-admin")
    public String createCouncilAdmin(
            @PathVariable String municipality,
            Model model,
            @Valid @ModelAttribute("councilAdmin") CouncilAdminViewModel viewModel,
            BindingResult errors,
            HttpServletRequest request) {

        if (errors.hasErrors()) {

            return "createCouncilAdmin";
        } else {
            UserRegisterViewModel userRegisterViewModel = new UserRegisterViewModel();
            userRegisterViewModel.setEmail(viewModel.getEmail());
            userRegisterViewModel.setUsername(viewModel.getEmail());
            userRegisterViewModel.setPassword(viewModel.getPassword());
            userService.save(userRegisterViewModel);
        }

        return "redirect:/";
    }

}
