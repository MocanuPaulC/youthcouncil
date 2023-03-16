package be.kdg.youthcouncil.controllers.mvc.viewModels;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class YouthCouncilCreateModel {

	@NotBlank (message = "Council Name is required")
	@Size (min = 3, max = 100, message = "Council Name should have length between 8 and 100")
	private String councilName;
	@NotBlank (message = "Municipality name is required")
	@Size (min = 3, max = 100, message = "Municipality name should have length between 8 and 100")
	private String municipality;

	//TODO add default description
	private String description = "";

	//TODO add default council Logo
	private String councilLogo = "";
	private boolean isAfterElection = false;

}
