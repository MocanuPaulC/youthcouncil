package be.kdg.youthcouncil.exceptions;

public class IdeaReactionNotFoundException extends RuntimeException {
	public IdeaReactionNotFoundException(long id) {
		super("The idea with Id " + id + " has no reactions or doesn't exist");
	}

	public IdeaReactionNotFoundException() {
		super("No reaction found");
	}
}
