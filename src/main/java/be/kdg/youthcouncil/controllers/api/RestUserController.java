package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.users.*;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.UpdateUserRoleDTO;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.exceptions.UsernameAlreadyExistsException;
import be.kdg.youthcouncil.service.users.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping ("/api/users")
public class RestUserController {
	private final UserService userService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder encoder;

	@DeleteMapping ("/{userId}")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable long userId) {
		if (userService.deleteUser(userId)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	@PatchMapping ("/{userId}/role")
	public ResponseEntity<UserDTO> updateUser(@PathVariable long userId, @RequestBody UpdateUserRoleDTO updateUserRoleDTO) {
		logger.debug(updateUserRoleDTO.toString());

		if (userService.updateRole(userId, updateUserRoleDTO.getRole(), updateUserRoleDTO.getYouthCouncilId())) {
			UserDTO userDTO = new UserDTO();
			userDTO.setRole(updateUserRoleDTO.getRole());
			return new ResponseEntity<>(userDTO, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PatchMapping ("/{userId}/blocked-status")
	public ResponseEntity<UpdateUserBlockedStatusDto> updateUserBlockedStatus(@PathVariable long userId, @RequestBody UpdateUserBlockedStatusDto updateUserBlockedStatusDto) {
		logger.debug(updateUserBlockedStatusDto.toString());

		if (userService.updateBlockedStatus(userId, updateUserBlockedStatusDto.isBlockedStatus(), updateUserBlockedStatusDto.getYouthCouncilId())) {
			return new ResponseEntity<>(updateUserBlockedStatusDto, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping ("{userId}/password")
	public ResponseEntity<UserResponseDto> resetPassword(@PathVariable long userId, @Valid @RequestBody RequestResetPasswordDTO requestResetPasswordDto, Principal principal) {
		PlatformUser user = userService.findUserByUsername(principal.getName());

		if (userId != user.getId()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		String passwd1 = requestResetPasswordDto.getPassword();
		String passwd2 = requestResetPasswordDto.getConfirmPassword();
		if (!passwd1.equals(passwd2)) new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		user.setPassword(encoder.encode(passwd1));
		userService.save(user);

		return ResponseEntity.ok(modelMapper.map(user, UserResponseDto.class));
	}

	@PatchMapping ("/{userId}/username")
	public ResponseEntity<ChangeUsernameDTO> changeUsername(@PathVariable long userId, @Valid @RequestBody RequestChangeUsernameDTO requestChangeUsernameDTO, Principal principal) {
		PlatformUser user = userService.findUserByUsername(principal.getName());

		if (userId != user.getId()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		try {
			userService.updateUsername(user.getUsername(), requestChangeUsernameDTO.getNewUsername());
		} catch (UsernameAlreadyExistsException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(new ChangeUsernameDTO(requestChangeUsernameDTO.getNewUsername()));
	}

}
