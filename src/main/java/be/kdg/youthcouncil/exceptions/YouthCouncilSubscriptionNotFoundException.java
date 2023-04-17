package be.kdg.youthcouncil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "Subscription not found")
public class YouthCouncilSubscriptionNotFoundException extends RuntimeException {
	public YouthCouncilSubscriptionNotFoundException(long id) {
		super(String.format("Subscription with id: %d, not found", id));
	}

	public YouthCouncilSubscriptionNotFoundException(long userId, long youthCouncilId) {
		super(String.format("Subscription to youthCouncil: %d for user : %d, not found", userId, youthCouncilId));
	}

}
