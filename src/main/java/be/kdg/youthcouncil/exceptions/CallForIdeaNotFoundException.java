package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Action Point not found")
public class CallForIdeaNotFoundException extends RuntimeException {
	public CallForIdeaNotFoundException(long id) {
		super(String.format("Call for idea %d not found", id));
	}
}
