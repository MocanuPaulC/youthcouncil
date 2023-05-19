package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionPointDto {
	private long actionPointId;
	private String title;
}
