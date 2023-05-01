package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions.ReactionDto;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.exceptions.IdeaReactionNotFoundException;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.interactions.IdeaReactionService;
import be.kdg.youthcouncil.service.youthcouncil.modules.IdeaService;
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
@RequestMapping ("/api/idea-reaction")
public class RestIdeaReactionController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	IdeaService ideaService;
	IdeaReactionService ideaReactionService;
	UserService userService;
	ModelMapper modelMapper;

	@PostMapping ("/react")
	public ResponseEntity<ReactionDto> reactTo(@Valid @RequestBody ReactionDto reactionDto) {
		ReactionDto returnDto = modelMapper.map(ideaReactionService.save(modelMapper.map(reactionDto, IdeaReaction.class)), ReactionDto.class);
		returnDto.setReactionCount(
				ideaReactionService.getReactionCount(returnDto.getEntityReactedOnId()));
		return ResponseEntity.ok().body(returnDto);

	}

	@GetMapping ("/{ideaId}/{userId}")
	public ResponseEntity<ReactionDto> getReaction(@PathVariable long ideaId, @PathVariable long userId) {
		try {

			return ResponseEntity.ok()
			                     .body(
					                     modelMapper.map(
							                     ideaReactionService.findUserReactionToIdea(ideaId, userId), ReactionDto.class)
			                     );
		} catch (IdeaReactionNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
}
