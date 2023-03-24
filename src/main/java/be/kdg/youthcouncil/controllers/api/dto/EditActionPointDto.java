package be.kdg.youthcouncil.controllers.api.dto;

import be.kdg.youthcouncil.domain.moduleItems.Label;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditActionPointDto {

	private long id;
	private String title;
	private String description;

	private Label label;
}
