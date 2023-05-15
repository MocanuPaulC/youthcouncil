package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.InformativePageBlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.InformativePageDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import be.kdg.youthcouncil.service.youthcouncil.modules.BlockService;
import be.kdg.youthcouncil.service.youthcouncil.modules.InformativePageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Validated
@RequestMapping ("/api/informativepages")
@RestController
@AllArgsConstructor
public class RestInformativePageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final InformativePageService informativePageService;
	private final BlockService blockService;

	@PostMapping ("/{title}")
	public ResponseEntity<InformativePageDto> createDefaultInfoPage(
			@PathVariable String title,
			@RequestBody @Valid List<@Valid InformativePageBlockDto> infoPageBlocks
	) {
		if (informativePageService.exists(Optional.empty(), title)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		informativePageService.save(title, true, infoPageBlocks, Optional.empty());
		return ResponseEntity.ok(new InformativePageDto(title));
	}


	@PutMapping ("/{title}")
	public ResponseEntity<InformativePageDto> editYouthCouncilInfoPage(
			@PathVariable String title,
			@RequestBody @Valid List<@Valid InformativePageBlockDto> infoPageBlocks
	) {

		informativePageService.save(title, true, infoPageBlocks, Optional.empty());
		return ResponseEntity.ok(new InformativePageDto(title));
	}

	@PostMapping ("/{municipality}/{title}")
	public ResponseEntity<InformativePageDto> createYouthCouncilInfoPage(
			@PathVariable String title,
			@PathVariable String municipality,
			@RequestBody @Valid List<@Valid InformativePageBlockDto> infoPageBlocks
	) {
		if (informativePageService.exists(Optional.of(municipality), title)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		informativePageService.save(title, false, infoPageBlocks, Optional.ofNullable(municipality));
		return ResponseEntity.ok(new InformativePageDto(title));
	}


	@PutMapping ("/{municipality}/{title}")
	public ResponseEntity<InformativePageDto> editYouthCouncilInfoPage(
			@PathVariable String title,
			@PathVariable String municipality,
			@RequestBody @Valid List<@Valid InformativePageBlockDto> infoPageBlocks
	) {

		informativePageService.save(title, false, infoPageBlocks, Optional.ofNullable(municipality));
		return ResponseEntity.ok(new InformativePageDto(title));
	}

	@GetMapping ("/informativepageblocks/{municipality}/{title}")
	public ResponseEntity<List<InformativePageBlockDto>> getInformativePageBlocks(
			@PathVariable String municipality,
			@PathVariable String title
	) {
		return ResponseEntity.ok(informativePageService.findInfoPageBlocks(Optional.of(municipality), title));
	}

	@GetMapping ("/informativepageblocks/{title}")
	public ResponseEntity<List<InformativePageBlockDto>> getInformativePageBlocks(
			@PathVariable String title
	) {
		List<InformativePageBlockDto> list = informativePageService.findInfoPageBlocks(Optional.empty(), title);
		logger.debug(list.toString());
		return ResponseEntity.ok(list);
	}

	@GetMapping ("/blocktypes")
	public List<String> getBlockTypes() {
		logger.debug("getting block types");
		logger.debug(Arrays.stream(BlockType.values()).map(BlockType::toString).toList().toString());
		return Arrays.stream(BlockType.values()).map(BlockType::toString).toList();
	}
}
