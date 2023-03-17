package be.kdg.youthcouncil.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResetPasswordDto {

	private String password;
	private String confirmPassword;

}
