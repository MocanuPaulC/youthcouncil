package be.kdg.youthcouncil.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
	private final long userId;


	public CustomUserDetails(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = id;
	}

	public long getUserId() {
		return userId;
	}


}
