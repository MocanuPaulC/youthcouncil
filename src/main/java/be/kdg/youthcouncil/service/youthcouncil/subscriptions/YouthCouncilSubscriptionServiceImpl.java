package be.kdg.youthcouncil.service.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class YouthCouncilSubscriptionServiceImpl implements YouthCouncilSubscriptionService{

    private final YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;

    @Override
    public YouthCouncilSubscription create(YouthCouncilSubscription subscription) {
        return youthCouncilSubscriptionRepository.save(subscription);
    }
}
