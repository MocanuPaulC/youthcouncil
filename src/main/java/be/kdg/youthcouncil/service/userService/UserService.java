package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.user.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
	List<User> getAllUsers();

	void create(UserRegisterViewModel userViewModel);


	public void processOAuthPostLogin(String username, Map<String, Object> attributes, String clientName);

	void save(UserRegisterViewModel userViewModel);
    Optional<User> findUserByUsername(String username);
}
