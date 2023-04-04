package be.kdg.youthcouncil.domain.users;

public interface Authenticable {

	long getId();

	boolean isGA();

	String getUsername();

	String getPassword();

	AuthenticationType getAuthenticationType();


}
