package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions.ActionPointReactionDto;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.exceptions.ActionPointReactionNotFound;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.interactions.ActionPointReactionService;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Getter
@Setter
@RequestMapping ("/api/actionpointreaction")
public class RestActionPointReactionController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	ActionPointService actionPointService;
	ActionPointReactionService actionPointReactionService;
	UserService userService;
	ModelMapper modelMapper;

	@PostMapping ("/react")
	public ResponseEntity<ActionPointReactionDto> reactTo(@Valid @RequestBody ActionPointReactionDto actionPointReactionDto) {
		ActionPointReactionDto returnDto = modelMapper.map(actionPointReactionService.save(modelMapper.map(actionPointReactionDto, ActionPointReaction.class)), ActionPointReactionDto.class);
		actionPointReactionService.addReactionCount(returnDto);
		return ResponseEntity.ok().body(returnDto);

	}

	@GetMapping ("/{actionPointId}/{userId}")
	public ResponseEntity<ActionPointReactionDto> getReaction(@PathVariable long actionPointId, @PathVariable long userId) {
		try {
			return ResponseEntity.ok()
			                     .body(modelMapper.map(actionPointReactionService.findUserReactionToActionPoint(actionPointId, userId), ActionPointReactionDto.class));
		} catch (ActionPointReactionNotFound e) {
			return ResponseEntity.notFound().build();
		}

	}

}
