package be.kdg.youthcouncil.controllers.mvc;


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
@RequestMapping("/youthcouncils")
@AllArgsConstructor
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

    @GetMapping("/add")
    public String getAddYouthCouncil(Model model) {
        model.addAttribute("youthCouncil", new NewYouthCouncilViewModel());
        return "addYouthCouncil";
    }

    @PostMapping("/add")
    public String addYouthCouncil(@Valid @ModelAttribute("youthCouncil") NewYouthCouncilViewModel viewModel, BindingResult errors, HttpServletRequest request) {
        logger.debug(viewModel.toString() + " in postMapping of addYouthCouncil");
        if (errors.hasErrors()) {
            return "addYouthCouncil";
        }
        youthCouncilService.create(viewModel);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getYouthCouncil(@PathVariable long id, Model model) {
        model.addAttribute("council", youthCouncilService.getYouthCouncil(id));
        logger.debug(youthCouncilService.getYouthCouncil(id).toString());

        return "youthCouncil";
    }

    @GetMapping("/{id}/create-council-admin")
    public String getCreateCouncilAdmin(@PathVariable long id, Model model) {
        model.addAttribute("council", youthCouncilService.getYouthCouncil(id));
        logger.debug(youthCouncilService.getYouthCouncil(id).toString());
        model.addAttribute("councilAdmin", new CouncilAdminViewModel());
        return "createCouncilAdmin";
    }

    @PostMapping("/{id}/createCouncilAdmin")
    public String createCouncilAdmin(
            @PathVariable long id,
            Model model,
            @Valid @ModelAttribute("councilAdmin") CouncilAdminViewModel viewModel,
            BindingResult errors,
            HttpServletRequest request) {

        logger.debug(viewModel.toString() + " in postMapping of createCouncilAdmin");
        logger.debug(errors.toString());
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
