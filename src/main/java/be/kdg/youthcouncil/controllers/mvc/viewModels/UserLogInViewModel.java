package be.kdg.youthcouncil.controllers.mvc.viewModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserLogInViewModel {

	@NotBlank (message = "Username is required")
	private String username;

	@NotBlank (message = "Password is required")
	private String password;


}
