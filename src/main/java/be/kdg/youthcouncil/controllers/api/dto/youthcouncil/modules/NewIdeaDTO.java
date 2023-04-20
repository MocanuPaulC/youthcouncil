package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewIdeaDTO {
	private long userId;
	private long callForIdeaId;
	@NotBlank
	private String idea;
	@NotNull
	private long subThemeId;
}
