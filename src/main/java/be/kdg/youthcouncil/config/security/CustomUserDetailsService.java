package be.kdg.youthcouncil.config.security;

import be.kdg.youthcouncil.domain.users.Authenticable;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("Loading user by username " + username);
		Authenticable user = userService.findAuthenticableByUsername(username);
		PlatformUser platformUser = userService.findByIdWithYouthCouncilSubscriptions(user.getId());

		List<GrantedAuthority> authorities = new ArrayList<>();


		if (user.isGA()) {
			authorities.addAll(Arrays.asList(
					new SimpleGrantedAuthority("ROLE_GENERAL_ADMIN"),
					new SimpleGrantedAuthority("ROLE_COUNCIL_ADMIN"),
					new SimpleGrantedAuthority("ROLE_MODERATOR"),
					new SimpleGrantedAuthority("ROLE_USER"),
					new SimpleGrantedAuthority("ROLE_OWNER")
			));
		} else {
			AtomicBoolean isCA = new AtomicBoolean(false);
			AtomicBoolean isModerator = new AtomicBoolean(false);
			AtomicBoolean isUser = new AtomicBoolean(false);

			platformUser.getYouthCouncilSubscriptions().forEach(subscription -> {
				if (!subscription.isDeleted()) {
					String role = subscription.getRole().toString();
					String municipality = subscription.getYouthCouncil().getMunicipality();

					switch (role) {
						case "COUNCIL_ADMIN" -> {
							if (!isCA.get()) {
								authorities.addAll(Arrays.asList(
										new SimpleGrantedAuthority("ROLE_COUNCIL_ADMIN"),
										new SimpleGrantedAuthority("ROLE_MODERATOR"),
										new SimpleGrantedAuthority("ROLE_USER")
								));
								isCA.set(true);
							}
							authorities.add(new SimpleGrantedAuthority("COUNCIL_ADMIN@" + municipality));
						}
						case "USER" -> {
							if (!isUser.get()) {
								authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
								isUser.set(true);
							}
							authorities.add(new SimpleGrantedAuthority("USER@" + municipality));
						}
						case "MODERATOR" -> {
							if (!isModerator.get()) {
								authorities.addAll(Arrays.asList(
										new SimpleGrantedAuthority("ROLE_MODERATOR"),
										new SimpleGrantedAuthority("ROLE_USER")
								));
								isModerator.set(true);
							}
							authorities.add(new SimpleGrantedAuthority("MODERATOR@" + municipality));
						}
					}
				}
			});
		}

		authorities.add(new SimpleGrantedAuthority(String.format("OWNER@%s", username)));
		authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
		logger.debug("User found");
		logger.debug("User: " + user.getUsername() + " " + user.getPassword());
		logger.debug("Authorities: " + authorities);

		return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.isGA(), authorities);
	}
}
