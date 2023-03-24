package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Action Point not found")
public class CallForIdeaNotFound extends RuntimeException {
	public CallForIdeaNotFound(long id) {
		super(String.format("Call for idea %d not found", id));
	}
}
