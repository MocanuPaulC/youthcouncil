package be.kdg.youthcouncil.service.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;

import java.util.List;

public interface YouthCouncilSubscriptionService {

	void create(long youthCouncilId, long userId);

	List<YouthCouncilSubscription> findAllByYouthCouncil(YouthCouncil youthCouncil);

	YouthCouncilSubscription create(YouthCouncilSubscription subscription);

	List<YouthCouncilSubscription> findAll();

	List<YouthCouncilSubscription> findAllYouthCouncilSubscriptionsWithUniqueUsers();

	YouthCouncilSubscription findAllByUserIdAndYouthCouncilId(long userId, long youthCouncilId);

	YouthCouncilSubscription findAllByUserIdAndYouthCouncilMunicipality(long userId, String s);
}
