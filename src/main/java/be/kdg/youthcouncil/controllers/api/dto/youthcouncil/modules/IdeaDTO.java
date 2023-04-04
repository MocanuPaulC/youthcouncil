package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IdeaDTO {
	private String idea;
	private String imagePath;
	private String username;

}
