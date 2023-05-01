package be.kdg.youthcouncil.exceptions;

public class IdeaNotFoundException extends RuntimeException {
	public IdeaNotFoundException(long id) {
		super(String.format("Idea %d not found", id));
	}

}
