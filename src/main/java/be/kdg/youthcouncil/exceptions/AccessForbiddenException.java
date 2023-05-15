package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.FORBIDDEN, reason = "You are not allowed to access this page! Sorry!")
public class AccessForbiddenException extends RuntimeException {
	public AccessForbiddenException() {
		super("You are not allowed to access this page! Sorry!");
	}
}
