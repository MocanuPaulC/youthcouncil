package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.user.User;

import java.util.List;
import java.util.Map;

public interface UserService {
	List<User> getAllUsers();

	void create(UserRegisterViewModel userViewModel);

	User findUserByUsername(String username);

	public void processOAuthPostLogin(String username, Map<String, Object> attributes, String clientName);

	void save(UserRegisterViewModel userViewModel);
}
