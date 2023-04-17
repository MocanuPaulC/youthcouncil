package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class YouthCouncilRepositoryTest {

	@Autowired
	private YouthCouncilRepository youthCouncilRepository;
	//	@Autowired
	//	private YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	//	@Autowired
	//	private MediaRepository mediaRepository;
	//	@Autowired
	//	private UserRepository userRepository;

	//	@BeforeEach
	//	public void setUp() {
	//		final Image media = mediaRepository.save(new Image("testpath"));
	//		PlatformUser user = userRepository.save(new PlatformUser("firstname", "lastname", "email", "2018", "username", "password"));
	//		YouthCouncil youthCouncil = youthCouncilRepository.save(new YouthCouncil("testName", "testMunicipality", "testDescription", media, false));
	//		YouthCouncilSubscription subscription = youthCouncilSubscriptionRepository.save(new YouthCouncilSubscription(user, youthCouncil, SubscriptionRole.COUNCIL_ADMIN));
	//	}
	//
	//	@AfterEach
	//	public void tearDown() {
	//		youthCouncilRepository.deleteAll();
	//	}

	@Test
	public void findAllFindsAll() {
		List<YouthCouncil> list = youthCouncilRepository.findAll();
		assertEquals(2, list.size());
	}

	@Test
	public void findYouthCouncilWithActionPointsReturnsWithActionPoints() {
		YouthCouncil yc = youthCouncilRepository.findById(1L).get();

		Optional<YouthCouncil> optionalYouthCouncil = youthCouncilRepository.findWithActionPoints(yc.getYouthCouncilId());
		final YouthCouncil youthCouncil = assertDoesNotThrow(optionalYouthCouncil::get);
		assertSame(youthCouncil.getActionPoints().get(0).getClass(), ActionPoint.class);
	}

	@Test
	public void findYouthCouncilWithSubscriptions() {
		YouthCouncil yc = youthCouncilRepository.findById(1L).get();

		Optional<YouthCouncil> optionalYouthCouncil = youthCouncilRepository.findWithSubscriptions(yc.getYouthCouncilId());
		final YouthCouncil youthCouncil = assertDoesNotThrow(optionalYouthCouncil::get);
		assertSame(youthCouncil.getSubscriptions()
		                       .get(0)
		                       .getClass(), YouthCouncilSubscription.class);
	}
}
