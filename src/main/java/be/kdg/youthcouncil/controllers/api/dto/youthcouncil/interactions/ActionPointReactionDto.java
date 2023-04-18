package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.youthcouncil.interactions.Emoji;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionPointReactionDto {

	private long actionPointReactedOnId;
	private Emoji reaction;
	private long reactingUserId;
	private long reactionCount;

}
