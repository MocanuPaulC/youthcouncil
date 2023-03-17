package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.RequestResetPasswordDto;
import be.kdg.youthcouncil.controllers.api.dto.UserResponseDto;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.service.userService.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/users/")
public class RestUserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserService userService;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder encoder;

	@PostMapping ("{userId}/resetpassword")
	public ResponseEntity<UserResponseDto> resetPassword(@PathVariable long userId, @Valid @RequestBody RequestResetPasswordDto requestResetPasswordDto, Principal principal) {
		User user = userService.findUserByUsername(principal.getName())
		                       .orElseThrow(() -> new UsernameNotFoundException("User could not be found!"));

		if (userId != user.getId()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		String passwd1 = requestResetPasswordDto.getPassword();
		String passwd2 = requestResetPasswordDto.getConfirmPassword();
		if (!passwd1.equals(passwd2)) new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		user.setPassword(encoder.encode(passwd1));
		userService.save(user);

		return ResponseEntity.ok(modelMapper.map(user, UserResponseDto.class));
	}
}
