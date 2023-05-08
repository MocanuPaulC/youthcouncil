package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class InformativePageRepositoryTest {

	@Autowired
	YouthCouncilRepository youthCouncilRepository;
	@Autowired
	InformativePageRepository informativePageRepository;

	@Test
	public void getInformativePageForInexistantInformativepageReturnsEmptyOptional() {
		String title = "ousefnspefm";
		YouthCouncil yc = youthCouncilRepository.findAll().get(0);
		assertEquals(Optional.empty(), (informativePageRepository.findByTitleAndMunicipality(title, yc.getMunicipality())));
	}
}
