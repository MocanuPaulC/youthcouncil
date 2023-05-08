package be.kdg.youthcouncil.service.users;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.users.Authenticable;
import be.kdg.youthcouncil.domain.users.AuthenticationType;
import be.kdg.youthcouncil.domain.users.GeneralAdmin;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.UserNotFoundException;
import be.kdg.youthcouncil.exceptions.UsernameAlreadyExistsException;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.persistence.users.AdminRepository;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.NotificationRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import be.kdg.youthcouncil.utility.Notification;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final NotificationRepository notificationRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void processOAuthPostLogin(String email, Map<String, Object> attributes, String clientName) {

		logger.debug("Processing OAuth post login for user: " + email);

		if (userRepository.findByEmail(email).isEmpty()) {
			PlatformUser newUser = new PlatformUser();
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
			newUser.setAuthenticationType(AuthenticationType.valueOf(clientName.toUpperCase()));
			userRepository.save(newUser);
		}
	}


	@Override
	public void markAllAsReadForUser(long userId) {
		PlatformUser user = userRepository.findByIdWithNotificationsNotSeen(userId)
		                                  .orElseThrow(() -> new UserNotFoundException(userId));

		List<Notification> notifications = user.getNotifications();
		for (Notification notification : notifications) {
			notification.setRead(true);
			notificationRepository.save(notification);
		}

	}

	@Override
	public boolean hasSubscriptionToActionPoint(long userId, long actionPointId) {
		return userRepository.findByIdWithActionPointSubscriptions(userId)
		                     .orElseThrow(() -> new UserNotFoundException(userId))
		                     .getActionPointSubscriptions()
		                     .stream()
		                     .map(ActionPointSubscription::getActionSubscriptionId)
		                     .anyMatch(a -> a == actionPointId);
	}

	@Override
	public void save(PlatformUser user) {
		userRepository.save(user);
	}

	@Override
	public PlatformUser findById(long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public boolean updateRole(long userId, String role) {
		try {
			userRepository.findById(userId).ifPresent(userRepository::save);
		} catch (HttpClientErrorException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Override
	public SubscriptionRole findSubscriptionRoleOfUserToYouthCouncil(long userId, long youthCouncilId) {
		return userRepository
				.findByIdWithYouthCouncilSubscriptions(userId)
				.flatMap(user -> user.getYouthCouncilSubscriptions().stream()
				                     .filter(subscription -> subscription.getYouthCouncil()
				                                                         .getYouthCouncilId() == youthCouncilId)
				                     .findFirst()
				                     .map(YouthCouncilSubscription::getRole))
				.orElseThrow(() -> new YouthCouncilSubscriptionNotFoundException(userId, youthCouncilId));
	}

	@Override
	public boolean isUserAdminOfYouthCouncil(long userId, long youthCouncilId) {
		return userRepository
				.findByIdWithYouthCouncilSubscriptions(userId)
				.map(user -> user.getYouthCouncilSubscriptions().stream()
				                 .anyMatch(subscription ->
						                 subscription
								                 .getRole().equals(SubscriptionRole.COUNCIL_ADMIN)
								                 && subscription.getYouthCouncil().getYouthCouncilId() == youthCouncilId
				                 ))
				.orElse(false);
	}

	@Transactional
	@Override
	public List<Notification> findLatest10AllNotifications(long id) {
		return new ArrayList<>(userRepository.findById(id)
		                                     .orElseThrow(() -> new UserNotFoundException(id))
		                                     .getNotifications().stream()
		                                     .sorted(Comparator.comparing(Notification::getDateTime).reversed())
		                                     .limit(10)
		                                     .toList());
	}


	@Override
	public void updateUsername(String oldUsername, String newUsername) {
		if (checkIfUserExists(newUsername)) throw new UsernameAlreadyExistsException(newUsername);
		PlatformUser user = userRepository.findByUsername(oldUsername)
		                                  .orElseThrow(() -> new UsernameNotFoundException(oldUsername));
		user.setUsername(newUsername);
		userRepository.save(user);
	}

	@Override
	public void create(UserRegisterViewModel userViewModel) {
		logger.debug("Saving user");
		userViewModel.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
		userRepository.save(modelMapper.map(userViewModel, PlatformUser.class));
	}

	@Override
	public List<PlatformUser> findAllUsers() {
		logger.debug("Getting all users");
		return userRepository.findAll();
	}

	public void save(UserRegisterViewModel user) {
		userRepository.save(modelMapper.map(user, PlatformUser.class));
	}

	@Override
	public PlatformUser findUserByUsername(String username) {
		return userRepository.findByUsername(username)
		                     .orElseThrow(() -> new UserNotFoundException(username));
	}

	@Override
	public PlatformUser findWithSubscriptionsAndYouthCouncils(String username) {
		PlatformUser user = userRepository.findWithSubscriptions(username)
		                                  .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
		user.getYouthCouncilSubscriptions().forEach(s -> {
			YouthCouncilSubscription youthCouncilSubscription = youthCouncilSubscriptionRepository.findWithYouthCouncil(s.getYouthCouncilSubscriptionId())
			                                                                                      .orElseThrow(() -> new YouthCouncilSubscriptionNotFoundException(s.getYouthCouncilSubscriptionId()));
			s.setYouthCouncil(youthCouncilSubscription.getYouthCouncil());
		});
		return user;
	}

	@Override
	public PlatformUser findByIdWithYouthCouncilSubscriptions(long id) {
		return userRepository.findByIdWithYouthCouncilSubscriptions(id)
		                     .orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public GeneralAdmin findAdminByUsername(String username) {
		return adminRepository.findByUsername(username)
		                      .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
	}

	@Override
	public Authenticable findAuthenticableByUsername(String username) {
		Optional<GeneralAdmin> possibleAdmin = adminRepository.findByUsername(username);
		if (possibleAdmin.isPresent()) return possibleAdmin.get();
		return userRepository.findByUsername(username)
		                     .orElseThrow(() -> new UsernameNotFoundException("The username could not be found!"));
	}

	@Override
	public List<PlatformUser> findAllWithIdeas() {
		return userRepository.findAllWithIdeas();
	}

	@Override
	public boolean checkIfAuthenticableExists(String username) {
		Optional<PlatformUser> possibleUser = userRepository.findByUsername(username);
		if (possibleUser.isPresent()) return true;
		Optional<GeneralAdmin> possibleAdmin = adminRepository.findByUsername(username);
		return possibleAdmin.isPresent();
	}

	@Override
	public boolean checkIfUserExists(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	@Override
	public boolean checkIfAdminExists(String username) {
		return adminRepository.findByUsername(username).isPresent();
	}

}
