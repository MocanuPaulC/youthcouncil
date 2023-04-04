package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private long youthCouncilId;
    private String youthCouncilName;
    private String subscriptionRole;
    private long subscriberId;
}
