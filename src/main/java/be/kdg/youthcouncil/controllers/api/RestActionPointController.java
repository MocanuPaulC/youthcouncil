package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.UpdateDisplayStatusDTO;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Getter
@Setter
@RequestMapping ("/api/actionpoints")
public class RestActionPointController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final YouthCouncilService youthCouncilService;

	private final ActionPointService actionPointService;

	@PutMapping ("/{youthCouncilId}/{actionPointId}")
	public ResponseEntity<EditActionPointDto> replaceActionPoint(@PathVariable long youthCouncilId, @PathVariable long actionPointId, @Valid @RequestBody EditActionPointDto editActionPointDto) {
		logger.debug("in updateActionPoint");
		logger.debug("actionPointId: " + actionPointId);
		logger.debug("youthCouncil: " + youthCouncilId);
		EditActionPointDto actionPointToReturn = actionPointService.update(actionPointId, youthCouncilId, editActionPointDto);

		return ResponseEntity.ok(actionPointToReturn);
	}

	@PostMapping ("/subscribe/{userId}/{actionPointId}")
	public ResponseEntity<String> subscribe(@PathVariable long userId, @PathVariable long actionPointId) {
		logger.debug("in subscribe");
		logger.debug("userId: " + userId);
		logger.debug("actionPointId: " + actionPointId);
		actionPointService.subscribe(userId, actionPointId);
		return ResponseEntity.ok("Subscribed");
	}

	@DeleteMapping ("/subscribe/{userId}/{actionPointId}")
	public ResponseEntity<String> unsubscribe(@PathVariable long userId, @PathVariable long actionPointId) {
		logger.debug("in unsubscribe");
		logger.debug("userId: " + userId);
		logger.debug("actionPointId: " + actionPointId);
		actionPointService.unsubscribe(userId, actionPointId);
		return ResponseEntity.ok("Unsubscribed");
	}

	@PatchMapping ("/{actionPointId}/set-display")
	public void setDisplay(@PathVariable long actionPointId, @RequestBody UpdateDisplayStatusDTO displayStatusDTO) {
		logger.debug("in setDisplay");
		logger.debug("actionPointId: " + actionPointId);
		logger.debug("isDisplayed: " + displayStatusDTO.isDisplayed());
		actionPointService.setDisplay(actionPointId, displayStatusDTO.isDisplayed());

	}

}
