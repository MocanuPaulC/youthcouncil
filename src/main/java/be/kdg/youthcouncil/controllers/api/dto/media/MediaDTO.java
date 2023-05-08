package be.kdg.youthcouncil.controllers.api.dto.media;

import be.kdg.youthcouncil.domain.media.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {

	private String path;
	private MediaType mediaType;
}
