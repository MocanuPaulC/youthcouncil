package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.BAD_REQUEST, reason = "The parameters passed for the creation of the Informational page are not allowed")
public class InformativePageSetupMismatchException extends RuntimeException {

	public InformativePageSetupMismatchException(String municipality, String title) {
		super(String.format("Cannot create a default informative page %s for the municipality %s", title, municipality));
	}
}
