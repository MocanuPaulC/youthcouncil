package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Informative page does not exist!")
public class InformativePageNotFoundException extends RuntimeException {
	public InformativePageNotFoundException(String title) {
		super("Could not find default informative page with title: " + title + ".");
	}

	public InformativePageNotFoundException(String title, String municipality) {
		super("Could not find informative page with title: " + title + " for the youthcouncil of the municipality " + municipality + ".");
	}

	public InformativePageNotFoundException(String title, Optional<String> municipality) {
		super(municipality.map(m -> "Could not find informative page with title: " + title + " for the youthcouncil of the municipality " + m + ".")
		                  .orElse("Could not find default informative page with title: " + title + "."));
	}
}
