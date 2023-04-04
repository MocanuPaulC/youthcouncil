package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.domain.users.Authenticable;
import be.kdg.youthcouncil.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public CustomUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		logger.debug("Loading user by username " + s);
		Authenticable user = userService.findAuthenticableByUsername(s);


		List<GrantedAuthority> authorities = new ArrayList<>();
		//TODO implement multi tenent system
		if (user.isGA()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_GENERAL_ADMIN"));
		} else {
			//FIXME how do we implement roles here
			authorities.add(new SimpleGrantedAuthority("ROLE_COUNCIL_ADMIN"));
		}
		logger.debug("User found");
		logger.debug("User: " + user.getUsername() + " " + user.getPassword());
		return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.isGA(),authorities);
	}
}
