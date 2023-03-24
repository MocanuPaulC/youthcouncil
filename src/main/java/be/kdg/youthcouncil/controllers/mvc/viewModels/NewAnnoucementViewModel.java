package be.kdg.youthcouncil.controllers.mvc.viewModels;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewAnnoucementViewModel {

	@NotBlank (message = "Title is required")
	@Size (min = 3, max = 100, message = "Title should have length between 8 and 100")
	private String title;

	@NotBlank (message = "Description is required")
	@Size (min = 8, message = "Description should have length between 8 and 100")
	private String description;


	private String images = "";

	private String videos = "";
}
