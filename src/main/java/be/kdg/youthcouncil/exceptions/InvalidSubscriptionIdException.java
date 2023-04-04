package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Subscription type does not exist!")
public class InvalidSubscriptionIdException extends RuntimeException{
    public InvalidSubscriptionIdException(int id) {
        super(String.format("The subscription with id: %d does not exist!", id));
    }
}
