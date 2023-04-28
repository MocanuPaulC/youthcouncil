package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.UpdateDisplayStatusDTO;
import be.kdg.youthcouncil.service.youthcouncil.modules.AnnouncementService;
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
@RequestMapping ("/api/announcements")
public class RestAnnouncementController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final AnnouncementService announcementService;

	@PatchMapping ("/{announcementId}/set-display")
	public void setDisplay(@PathVariable long announcementId, @RequestBody UpdateDisplayStatusDTO displayStatusDTO) {
		logger.debug("in setDisplay");
		logger.debug("actionPointId: " + announcementId);
		logger.debug("isDisplayed: " + displayStatusDTO.isDisplayed());
		announcementService.setDisplay(announcementId, displayStatusDTO.isDisplayed());

	}
}
