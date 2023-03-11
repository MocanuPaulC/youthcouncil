package be.kdg.youthcouncil.service.userService;

import be.kdg.youthcouncil.domain.user.CustomUserDetails;
import be.kdg.youthcouncil.domain.user.User;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.debug("Loading user by username "  + s);
        User user = userService.findUserByUsername(s);

        if(user!=null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            logger.debug("User found");
            logger.debug("User: " + user.getUsername() + " " + user.getPassword());
            return new CustomUserDetails(user.getId(),user.getUsername(),user.getPassword(),authorities);
        }else {
            logger.debug("User not found");
            throw new UsernameNotFoundException("User not found");

        }

    }

}
