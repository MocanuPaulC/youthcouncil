package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Sub Theme not found")
public class SubThemeNotFoundException extends RuntimeException {
	public SubThemeNotFoundException(long id) {
		super(String.format("Sub Theme with id %d could not be found", id));
	}
}
