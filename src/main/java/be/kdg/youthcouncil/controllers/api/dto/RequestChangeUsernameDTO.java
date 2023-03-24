package be.kdg.youthcouncil.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangeUsernameDTO {

	@NotBlank
	private String newUsername;
}
