package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewIdeaDTO {
	private long userId;
	private long callForIdeasId;
	private String idea;
	private long subThemeId;

}
