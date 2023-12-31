package be.kdg.youthcouncil.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "The municipality.json file could not be found!")
public class MunicipalitiesJsonNotFoundException extends RuntimeException {

	public MunicipalitiesJsonNotFoundException() {
		super("The municipality.json file could not be found! It should be under resources/municipality.json");
	}
}
