package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.user.AuthenticationType;
import be.kdg.youthcouncil.domain.user.Role;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void processOAuthPostLogin(String email, Map<String, Object> attributes, String clientName) {

		logger.debug("Processing OAuth post login for user: " + email);

		if (userRepository.findByEmail(email).isEmpty()) {
			User newUser = new User();
			newUser.setUsername(email);
			newUser.setEmail(email);
			if (clientName.equalsIgnoreCase("FACEBOOK")) {
				String[] names = ((String) attributes.get("name")).split(" ", 2);
				newUser.setFirstName(names[0]);
				newUser.setLastName(names[1]);
			} else {
				newUser.setLastName((String) attributes.get("family_name"));
				newUser.setFirstName((String) attributes.get("given_name"));
			}
			newUser.setAuthType(AuthenticationType.valueOf(clientName.toUpperCase()));
			newUser.setRole(Role.MEMBER);
			userRepository.save(newUser);
		}
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public boolean updateRole(long userId, String role) {
		try {
			userRepository.findById(userId).ifPresent(user -> {
				user.setRole(Role.valueOf(role));
				userRepository.save(user);
			});
		} catch (HttpClientErrorException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Override
	public void create(UserRegisterViewModel userViewModel) {
		logger.debug("Saving user");
		userViewModel.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
		userRepository.save(modelMapper.map(userViewModel, User.class));
	}

	@Override
	public List<User> findAllUsers() {
		logger.debug("Getting all users");
		return userRepository.findAll();
	}

	public void save(UserRegisterViewModel user) {
		userRepository.save(modelMapper.map(user, User.class));
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
		                     .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
	}

	@Override
	public List<User> findAllWithIdeas() {
		return userRepository.findAllWithIdeas();
	}
}
