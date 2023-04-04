package be.kdg.youthcouncil.controllers.api.dto.users;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

	private String username;
	private String password;
	private String role;
	private String email;
	private String firstName;
	private String lastName;
	private String postcode;
}
