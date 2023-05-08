package be.kdg.youthcouncil.service.media;

import be.kdg.youthcouncil.domain.media.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface MediaService {

	Optional<MediaType> getMediaType(String fileName);

	Optional<InputStream> getMediaAsStream(String fileName);

	String uploadMedia(MultipartFile file) throws IOException;
}
