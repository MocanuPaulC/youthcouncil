package be.kdg.youthcouncil.service.media;

import be.kdg.youthcouncil.domain.media.MediaType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String TARGET_DIR = new File("build/resources/main/static/images/uploads/").getAbsolutePath();

	@Override
	public Optional<MediaType> getMediaType(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		logger.debug(extension);
		return switch (extension) {
			case "mp4" -> Optional.of(MediaType.VIDEO);
			case "jpg", "png", "gif" -> Optional.of(MediaType.IMAGE);
			default -> Optional.empty();
		};

	}

	@Override
	public Optional<InputStream> getMediaAsStream(String fileName) {
		String imagePath = TARGET_DIR + "/" + fileName;
		logger.debug("looking for image: " + imagePath);
		try {
			return Optional.of(new FileInputStream(imagePath));
		} catch (FileNotFoundException e) {
			return Optional.empty();
		}
	}

	//FIXME maybe create a new implementation of this class for a actual bucket and not the local dir
	@Override
	public String uploadMedia(MultipartFile file) throws IOException {
		//TODO rename images to something programmatic so that there are no overlaps

		StringBuilder fileNames = new StringBuilder();
		Path fileNameAndPath = Paths.get(TARGET_DIR, file.getOriginalFilename());
		fileNames.append(file.getOriginalFilename());
		Files.write(fileNameAndPath, file.getBytes());
		return fileNames.toString();
	}
}
