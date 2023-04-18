package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Action Point not found")
public class ActionPointNotFoundException extends RuntimeException {
	public ActionPointNotFoundException(long id) {
		super(String.format("Action Point %d not found", id));
	}


}
