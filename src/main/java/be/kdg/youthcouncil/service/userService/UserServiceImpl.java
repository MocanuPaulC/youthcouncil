package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.UserRegisterViewModel;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.persistence.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        passwordEncoder=new BCryptPasswordEncoder();


    }

    @Override
    public void saveUser(UserRegisterViewModel userViewModel) {
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
}
