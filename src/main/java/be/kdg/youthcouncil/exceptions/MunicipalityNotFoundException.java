package be.kdg.youthcouncil.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Municipality not found")
public class MunicipalityNotFoundException extends RuntimeException {
	public MunicipalityNotFoundException(String municipality) {
		super(String.format("Municipality %s not found", municipality));
	}
}
