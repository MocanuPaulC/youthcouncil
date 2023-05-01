package be.kdg.youthcouncil.exceptions;

public class ActionPointReactionNotFoundException extends RuntimeException {
	public ActionPointReactionNotFoundException() {
		super("No reaction found");
	}
}
