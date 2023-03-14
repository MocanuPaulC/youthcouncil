package be.kdg.youthcouncil.controllers.mvc.viewModels;

import be.kdg.youthcouncil.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouncilAdminViewModel {
	@NotBlank (message = "Username is required")
	@Size (min = 3, max = 100, message = "Username should have length between 8 and 100")
	private String email;
	private String password = "password";
	private Role role = Role.COUNCIL_ADMIN;


}
