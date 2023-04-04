package be.kdg.youthcouncil.controllers.mvc.viewModels;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegisterViewModel {

	@NotBlank (message = "Username is required")
	@Size (min = 8, max = 100, message = "Username should have length between 8 and 100")
	private String username;
	@NotBlank (message = "Password is required")
	@Size (min = 8, max = 100, message = "Password should have length between 8 and 100")
	private String password;
	@NotBlank (message = "Password confirmation is required")
	@Size (min = 8, max = 100, message = "Password confirmation should have length between 8 and 100")
	private String passwordConfirm;

	@NotBlank (message = "Email is required")
	private String email;
	@NotBlank (message = "First name is required")
	@Size (min = 3, max = 100, message = "First name should have length between 3 and 100")
	private String firstName;
	@NotBlank (message = "Last name is required")
	@Size (min = 3, max = 100, message = "Last name should have length between 3 and 100")
	private String lastName;

	@NotBlank (message = "Street is required")
	@Pattern (regexp = "^[0-9]+$", message = "Postcode can only contain numbers")
	private String postcode;



}
