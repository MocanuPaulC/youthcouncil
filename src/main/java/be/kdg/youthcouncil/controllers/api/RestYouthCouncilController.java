package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.service.userService.UserService;
import be.kdg.youthcouncil.service.youthCouncilService.YouthCouncilService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/youthcouncils")
public class RestYouthCouncilController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final YouthCouncilService youthCouncilService;

	private final UserService userService;

	@PatchMapping ("/{youthCouncilId}/{userId}")
	public void addMemeberToYouthCouncil(Model model, @PathVariable long youthCouncilId, @PathVariable long userId) {

		logger.debug("Adding user +" + model.getAttribute("authUser") + " to youth council " + youthCouncilId);
		YouthCouncil youthCouncil = youthCouncilService.findByIdWithMembers(youthCouncilId);
		youthCouncil.addCouncilMember(userService.findById(userId));
		youthCouncilService.save(youthCouncil);

	}

	//TODO: implement
	//    @GetMapping
	//    public ResponseEntity<ActionPointDTO> getFilteredActionPoints(){
	//        return null;
	//    }

}
