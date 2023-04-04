package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPointStatus;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ActionPointRepositoryTest {

	@Autowired
	private ActionPointRepository actionPointRepository;

	@Autowired
	private YouthCouncilRepository youthCouncilRepository;

	@Autowired
	private SubThemeRepository subThemeRepository;


	@Test
	public void saveActionPointFromActionPointRepositorySavesActionPoint() {
		YouthCouncil youthCouncil = youthCouncilRepository.findAll().get(0);
		ActionPoint actionPoint = new ActionPoint("title", "sefsef", subThemeRepository.findAll()
		                                                                               .get(0), ActionPointStatus.NEW, youthCouncil, true);
		ActionPoint savedActionPoint = actionPointRepository.save(actionPoint);

		assertEquals(savedActionPoint.getActionPointId(), actionPointRepository.findById(savedActionPoint.getActionPointId())
		                                                                       .get()
		                                                                       .getActionPointId());

	}
}
