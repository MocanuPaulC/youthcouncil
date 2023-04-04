package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class YouthCouncilSubscriptionTest {

	@Autowired
	private YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void test() {
		youthCouncilSubscriptionRepository.findAll().forEach(x -> {
			System.out.println(x.getSubscriber().getUsername() + " " + x.getYouthCouncil().getCouncilName());
		});

		userRepository.findWithSubscriptions("cadmin1").get().getYouthCouncilSubscriptions().forEach(x -> {
			System.out.println(x.getYouthCouncil().getCouncilName());
		});
	}
}
