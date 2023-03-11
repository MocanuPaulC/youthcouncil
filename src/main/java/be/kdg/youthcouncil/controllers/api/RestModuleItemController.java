package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.service.moduleItemService.ModuleItemService;
import be.kdg.youthcouncil.service.userService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestModuleItemController {
    private final ModuleItemService moduleItemService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RestModuleItemController(ModuleItemService moduleItemService, UserService userService) {
        this.moduleItemService = moduleItemService;
        this.userService = userService;
    }

    //TODO: implement
    //    @PostMapping
    //    public ResponseEntity<IdeaDTO> answerCallForIdeas(){
    //        return null;
    //    }

}
