package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.domain.User;
import be.kdg.youthcouncil.persistence.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;

    }

    @Override
    public List<User> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll();
    }
}
