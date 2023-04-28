package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPointLabel;
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

	private long youthCouncilId;

	private ActionPointLabel status;
}
