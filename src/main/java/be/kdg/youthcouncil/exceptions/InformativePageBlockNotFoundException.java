package be.kdg.youthcouncil.exceptions;

public class InformativePageBlockNotFoundException extends RuntimeException {
	public InformativePageBlockNotFoundException(long id) {
		super("InfoPageBlock with id: " + id + " not found");
	}
}
