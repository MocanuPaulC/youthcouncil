package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.UpdateDisplayStatusDTO;
import be.kdg.youthcouncil.service.youthcouncil.modules.CallForIdeaService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Getter
@Setter
@RequestMapping ("/api/call-for-ideas")
public class RestCallForIdeaController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final CallForIdeaService callForIdeaService;

	@PatchMapping ("/{callForIdeaId}/set-display")
	public void setDisplay(@PathVariable long callForIdeaId, @RequestBody UpdateDisplayStatusDTO displayStatusDTO) {
		logger.debug("in setDisplay");
		logger.debug("actionPointId: " + callForIdeaId);
		logger.debug("isDisplayed: " + displayStatusDTO.isDisplayed());
		callForIdeaService.setDisplay(callForIdeaId, displayStatusDTO.isDisplayed());

	}
}
