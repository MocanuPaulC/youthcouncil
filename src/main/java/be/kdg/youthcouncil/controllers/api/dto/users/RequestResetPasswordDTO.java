package be.kdg.youthcouncil.controllers.api.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResetPasswordDTO {

	@NotBlank
	private String password;
	@NotBlank
	private String confirmPassword;

}
