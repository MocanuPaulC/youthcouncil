package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.user.AuthenticationType;
import be.kdg.youthcouncil.domain.user.Role;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.persistence.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		passwordEncoder = new BCryptPasswordEncoder();


	}


	@Override
	public void processOAuthPostLogin(String username, Map<String, Object> attributes, String clientName) {

		logger.debug("Processing OAuth post login for user: " + username);
		User existUser = userRepository.findByUsername(username);
		attributes.forEach((key, value) -> logger.debug("Key = " + key + ", Value = " + value));

		if (existUser == null) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setEmail(username);
			if (clientName.equalsIgnoreCase("FACEBOOK")) {
				String[] names = ((String) attributes.get("name")).split(" ");
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
	public void create(UserRegisterViewModel userViewModel) {
		logger.debug("Saving user");
		userViewModel.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
		userRepository.save(modelMapper.map(userViewModel, User.class));
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> getAllUsers() {
		logger.debug("Getting all users");
		return userRepository.findAll();
	}

	public void save(UserRegisterViewModel user) {
		userRepository.save(modelMapper.map(user, User.class));
	}

}
