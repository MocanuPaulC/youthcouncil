package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditActionPointDto;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
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
}
