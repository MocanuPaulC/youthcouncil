package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.IdeaDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.NewIdeaDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import be.kdg.youthcouncil.service.youthcouncil.modules.IdeaService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Getter
@Setter
@RequestMapping ("/api/ideas")
public class RestIdeaController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ModelMapper modelMapper;
	private final IdeaService ideaService;
	private final UserService userService;
	private final CallForIdeaService callForIdeaService;

	@PostMapping ("/{callForIdeaId}/uploadFile")
	public ResponseEntity<Boolean> uploadFile(@RequestParam ("file") MultipartFile file, @PathVariable long callForIdeaId) {
		if (!file.isEmpty()) {
			// Check if the file is a CSV
			boolean isCSV = callForIdeaService.processCSVUpload(file, callForIdeaId);
			if (isCSV) {
				return ResponseEntity.ok().body(isCSV);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@GetMapping ("/{callForIdeaId}/subthemes")
	public ResponseEntity<List<String>> getSubTheme(@PathVariable long callForIdeaId) {

		List<String> toReturn = callForIdeaService.findById(callForIdeaId)
		                                          .getTheme()
		                                          .getSubThemes()
		                                          .stream()
		                                          .map(SubTheme::getSubTheme)
		                                          .toList();
		return ResponseEntity.ok().body(toReturn);
	}

	@PostMapping
	public ResponseEntity<IdeaDTO> answerCFI(@Valid @RequestBody NewIdeaDTO newIdeaDTO) {

		return ResponseEntity.ok()
		                     .body(modelMapper.map(ideaService.createIdea(modelMapper.map(newIdeaDTO, Idea.class)), IdeaDTO.class));

	}
}
