package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.IdeaSubmissionDTO;
import be.kdg.youthcouncil.service.callForIdeaService.CallForIdeaService;
import be.kdg.youthcouncil.service.moduleItemService.ModuleItemService;
import be.kdg.youthcouncil.service.userService.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Getter
@Setter
@RequestMapping ("/api/ideas")
public class RestIdeaController {

	private final ModuleItemService moduleItemService;
	private final UserService userService;
	private final CallForIdeaService callForIdeaService;


	@PostMapping ("/{userId}/{callForIdeaId}")
	public ResponseEntity<IdeaSubmissionDTO> answerCFI(
			@PathVariable long userId,
			@PathVariable long callForIdeaId,
			@RequestBody IdeaSubmissionDTO ideaDTO) {

		return ResponseEntity.ok()
		                     .body(moduleItemService.handleCallForIdeas(ideaDTO.getTitle(), callForIdeaId, userId, "mockTheme"));

	}


}
