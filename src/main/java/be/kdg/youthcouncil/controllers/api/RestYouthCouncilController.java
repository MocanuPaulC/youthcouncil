package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/youthcouncils")
public class RestYouthCouncilController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final YouthCouncilService youthCouncilService;
	private final CallForIdeaService callForIdeaService;

	private final UserService userService;


	@PostMapping ("/{id}/callforideas")
	public ResponseEntity<CallForIdeasDTO> launchCallForIdeas(@PathVariable Long id, @Valid @RequestBody CallForIdeasDTO callForIdeasDTO) {
		callForIdeaService.create(callForIdeasDTO);
		return ResponseEntity.ok(callForIdeasDTO);
	}
}
