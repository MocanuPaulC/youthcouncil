package be.kdg.youthcouncil.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Municipality not found")
public class UserNotFound extends RuntimeException {
	public UserNotFound(String username) {
		super(String.format("User %s not found", username));
	}

	public UserNotFound(long id) {
		super(String.format("User %d not found", id));
	}
}
