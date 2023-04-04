package be.kdg.youthcouncil.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
	private final long userId;
	private final boolean isGa;

	public CustomUserDetails(long id, String username, String password, boolean isGa, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = id;
		this.isGa = isGa;
	}
}
