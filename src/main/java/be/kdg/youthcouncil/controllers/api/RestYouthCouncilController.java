package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.service.youthCouncilService.YouthCouncilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestYouthCouncilController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final YouthCouncilService youthCouncilService;

	public RestYouthCouncilController(YouthCouncilService youthCouncilService) {
		this.youthCouncilService = youthCouncilService;
	}

	//TODO: implement
	//    @GetMapping
	//    public ResponseEntity<ActionPointDTO> getFilteredActionPoints(){
	//        return null;
	//    }

}
