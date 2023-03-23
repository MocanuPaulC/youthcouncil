package be.kdg.youthcouncil.controllers.mvc.viewModels;

import be.kdg.youthcouncil.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouncilAdminViewModel {
	@NotBlank (message = "Email is required")
	@Email (message = "Should be an email")
	private String email;
	private String password = "password";
	private Role role = Role.COUNCIL_ADMIN;


}
