package be.kdg.youthcouncil.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Municipality not found")
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String username) {
		super(String.format("User %s not found", username));
	}

	public UserNotFoundException(long id) {
		super(String.format("User %d not found", id));
	}
}
