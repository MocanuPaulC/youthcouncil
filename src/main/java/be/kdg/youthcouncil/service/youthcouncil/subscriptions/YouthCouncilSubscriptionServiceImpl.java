package be.kdg.youthcouncil.service.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class YouthCouncilSubscriptionServiceImpl implements YouthCouncilSubscriptionService {

	private final YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	private final YouthCouncilService youthCouncilService;
	private final UserService userService;

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());
		return t -> seen.add(keyExtractor.apply(t));
	}

	@Override
	public void create(long youthCouncilId, long userId) {

		YouthCouncil youthCouncil = youthCouncilService.getYouthCouncil(youthCouncilId);
		PlatformUser platformUser = userService.findByIdWithYouthCouncilSubscriptions(userId);
		try {
			YouthCouncilSubscription existingSubscription = youthCouncilSubscriptionRepository.findBySubscriber_userIdAndYouthCouncil_YouthCouncilId(userId, youthCouncilId)
			                                                                                  .orElseThrow(() -> new YouthCouncilSubscriptionNotFoundException(userId, youthCouncilId));
			existingSubscription.setDeleted(false);
			youthCouncilSubscriptionRepository.save(existingSubscription);
		} catch (YouthCouncilSubscriptionNotFoundException e) {
			YouthCouncilSubscription subscription = new YouthCouncilSubscription(platformUser, youthCouncil, SubscriptionRole.USER);
			youthCouncilSubscriptionRepository.save(subscription);
		}
		userService.save(platformUser);
	}

	@Override
	public List<YouthCouncilSubscription> findAllByYouthCouncil(YouthCouncil youthCouncil) {
		return youthCouncilSubscriptionRepository.findAllByYouthCouncil(youthCouncil).stream().filter(
				youthCouncilSubscription -> !youthCouncilSubscription.isDeleted()).toList();
	}

	@Override
	public YouthCouncilSubscription create(YouthCouncilSubscription subscription) {
		return youthCouncilSubscriptionRepository.save(subscription);
	}

	@Override
	public List<YouthCouncilSubscription> findAll() {
		return youthCouncilSubscriptionRepository.findAll();
	}

	@Override
	public List<YouthCouncilSubscription> findAllYouthCouncilSubscriptionsWithUniqueUsers() {
		return youthCouncilSubscriptionRepository.findAll()
		                                         .stream()
		                                         .filter(youthCouncilSubscription -> !youthCouncilSubscription.isDeleted())
		                                         .filter(distinctByKey(YouthCouncilSubscription::getSubscriber))
		                                         .toList();

	}

	@Override
	public YouthCouncilSubscription findAllByUserIdAndYouthCouncilId(long userId, long youthCouncilId) {

		return youthCouncilSubscriptionRepository.findBySubscriber_userIdAndYouthCouncil_YouthCouncilId(userId, youthCouncilId)
		                                         .orElseThrow(
				                                         () -> new YouthCouncilSubscriptionNotFoundException(userId, youthCouncilId));
	}

	@Override
	public YouthCouncilSubscription findAllByUserIdAndYouthCouncilMunicipality(long userId, String s) {
		return youthCouncilSubscriptionRepository.findBySubscriber_userIdAndYouthCouncil_Municipality(userId, s)
		                                         .orElseThrow(
				                                         () -> new YouthCouncilSubscriptionNotFoundException(userId, s));
	}

	@Override
	@Transactional
	public void remove(long youthCouncilId, long userId) {
		youthCouncilSubscriptionRepository.deleteBySubscriber_userIdAndYouthCouncil_YouthCouncilId(userId, youthCouncilId);
	}


}
