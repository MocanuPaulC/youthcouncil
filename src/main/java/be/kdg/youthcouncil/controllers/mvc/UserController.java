package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserLogInViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.service.userService.UserService;
import be.kdg.youthcouncil.domain.user.Role;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView logIn(HttpServletRequest request, ModelAndView mav) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        mav.addObject("user",new UserLogInViewModel());
        mav.setViewName("login");
        return mav;
    }

    @GetMapping("/register")
    public String register(Model model) {
        logger.debug("in getMapping of register");
        model.addAttribute("user", new UserRegisterViewModel());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping("/register" )
    public String confirmRegister(@ModelAttribute("user")UserRegisterViewModel viewModel, HttpServletRequest request, BindingResult errors) {
        logger.debug(viewModel.toString()+" in postMapping of register");
        if(errors.hasErrors()) {
            return "register";
        }
        userService.saveUser(viewModel);
        return "redirect:/";
    }

}
