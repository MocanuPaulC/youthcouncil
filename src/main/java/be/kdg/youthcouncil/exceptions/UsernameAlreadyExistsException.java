package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.BAD_REQUEST, reason = "The username already exists!")
public class UsernameAlreadyExistsException extends RuntimeException{

	public UsernameAlreadyExistsException(String username) {super(String.format("The username %s already exists!", username));}
}
