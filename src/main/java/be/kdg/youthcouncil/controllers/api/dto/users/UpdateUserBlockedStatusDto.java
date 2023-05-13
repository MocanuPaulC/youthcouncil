package be.kdg.youthcouncil.controllers.api.dto.users;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserBlockedStatusDto {
	boolean blockedStatus;
	long youthCouncilId;
}
