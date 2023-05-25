package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Theme not found")
public class ThemeNotFoundException extends RuntimeException {
	public ThemeNotFoundException(long id) {
		super(String.format("Theme with id %d could not be found", id));
	}
}
