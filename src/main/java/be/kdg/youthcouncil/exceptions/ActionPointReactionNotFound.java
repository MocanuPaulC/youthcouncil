package be.kdg.youthcouncil.exceptions;

public class ActionPointReactionNotFound extends RuntimeException {
	public ActionPointReactionNotFound() {
		super("No reaction found");
	}
}
