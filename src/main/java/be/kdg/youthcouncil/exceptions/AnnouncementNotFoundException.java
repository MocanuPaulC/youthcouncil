package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Action Point not found")
public class AnnouncementNotFoundException extends RuntimeException {
	public AnnouncementNotFoundException(long id) {
		super("Announcement not found");
	}
}
