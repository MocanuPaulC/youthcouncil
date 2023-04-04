package be.kdg.youthcouncil.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND, reason = "YouthCouncilId could not be found")
public class YouthCouncilIdNotFoundException extends RuntimeException {
	public YouthCouncilIdNotFoundException(long youthCouncilId) {
		super(String.format("Municipality %d not found", youthCouncilId));
	}
}
