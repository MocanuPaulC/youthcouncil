package be.kdg.youthcouncil.service.users;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.users.Authenticable;
import be.kdg.youthcouncil.domain.users.GeneralAdmin;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.utility.Notification;

import java.util.List;
import java.util.Map;

public interface UserService {
	List<PlatformUser> findAllUsers();

	void create(UserRegisterViewModel userViewModel);

	PlatformUser findById(long id);

	public void processOAuthPostLogin(String username, Map<String, Object> attributes, String clientName);

	void save(UserRegisterViewModel userViewModel);

	boolean updateRole(long id, String role, long youthCouncilId);

	void save(PlatformUser user);

	PlatformUser findUserByUsername(String username);

	PlatformUser findWithSubscriptionsAndYouthCouncils(String username);

	GeneralAdmin findAdminByUsername(String username);

	Authenticable findAuthenticableByUsername(String username);

	void updateUsername(String oldUsername, String newUsername);

	List<PlatformUser> findAllWithIdeas();

	boolean checkIfAuthenticableExists(String username);

	boolean checkIfUserExists(String username);

	boolean checkIfAdminExists(String username);

	PlatformUser findByIdWithYouthCouncilSubscriptions(long id);

	SubscriptionRole findSubscriptionRoleOfUserToYouthCouncil(long userId, long youthCouncilId);

	boolean isUserAdminOfYouthCouncil(long userId, long youthCouncilId);

	List<Notification> findLatest10AllNotifications(long id);

	void markAllAsReadForUser(long id);

	boolean hasSubscriptionToActionPoint(long userId, long actionPointId);

	boolean updateBlockedStatus(long userId, boolean blocked, long youthCouncilId);

	boolean deleteUser(long userId);
}
