package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.media.MediaDTO;
import be.kdg.youthcouncil.domain.media.MediaType;
import be.kdg.youthcouncil.service.media.MediaService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping ("/api/media")
@RestController
public class RestMediaController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MediaService mediaService;

	@GetMapping ("/uploads/{imagename}")
	public ResponseEntity<InputStreamResource> getMedia(@PathVariable String imagename) {
		if (imagename.contains("..")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		if (!imagename.contains(".") || mediaService.getMediaType(imagename).isEmpty())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


		Optional<InputStream> in = mediaService.getMediaAsStream(imagename);


		logger.debug(in.toString());

		return in.map(inputStream -> ResponseEntity.ok()
		                                           .body(new InputStreamResource(inputStream)))
		         .orElseGet(() -> {
			         logger.debug("there is no in!");
			         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		         });

	}

	@PostMapping ("/upload")
	public ResponseEntity<MediaDTO> uploadMedia(@RequestParam ("media") MultipartFile media) throws IOException {
		logger.debug(media.getOriginalFilename());
		Optional<MediaType> type = mediaService.getMediaType(media.getOriginalFilename());
		logger.debug(type.toString());
		if (type.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String url = mediaService.uploadMedia(media);

		return ResponseEntity.ok(new MediaDTO(url, type.get()));
	}

}
