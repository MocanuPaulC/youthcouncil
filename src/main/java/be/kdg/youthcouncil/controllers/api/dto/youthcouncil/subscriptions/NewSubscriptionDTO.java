package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewSubscriptionDTO {
	private long userId;
	private long youthCouncilId;
	private int role = 2;
}
