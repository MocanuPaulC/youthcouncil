package be.kdg.youthcouncil.controllers.api.dto;

import be.kdg.youthcouncil.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private long id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String postcode;
	private Role role;

}
