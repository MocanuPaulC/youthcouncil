package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

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
public class CallForIdeasDTO {
	@NotBlank (message = "Title is required")
	@Size (min = 3, max = 100, message = "Title should have length between 3 and 100")
	private String title;
}
