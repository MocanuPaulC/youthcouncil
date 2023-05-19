package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePageBlock;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import be.kdg.youthcouncil.exceptions.InformativePageNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageBlockRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance (TestInstance.Lifecycle.PER_CLASS)
class InformativePageServiceImplTest {

	private final Logger logger = LoggerFactory.getLogger(InformativePageServiceImplTest.class);
	@Autowired
	private InformativePageBlockRepository blockRepository;

	@Autowired
	private InformativePageRepository informativePageRepository;

	@Autowired
	private InformativePageService informativePageService;
	@Autowired
	private YouthCouncilRepository youthCouncilRepository;

	@BeforeEach
	void setUp() {
		InformativePageBlock block1 = new InformativePageBlock();
		InformativePageBlock block2 = new InformativePageBlock();
		block1.setType(BlockType.HEADER_BIG);
		block1.setContent("test");
		block1.setOrderNumber(1);
		block2.setType(BlockType.PARAGRAPH);
		block2.setContent("testParagraph");
		block2.setOrderNumber(2);


		List<InformativePageBlock> blocks = new ArrayList<>();
		blocks.add(block1);
		blocks.add(block2);
		InformativePage informativePage = new InformativePage();
		informativePage.setTitle("test");
		informativePage.setDefault(true);
		informativePage.setInfoPageBlocks(blocks);
		blockRepository.saveAll(blocks);
		informativePageRepository.save(informativePage);
		logger.debug(informativePageRepository.findAll().toString());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void save() {
		YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityName("Mortsel").orElse(null);
		InformativePage informativePage = new InformativePage();
		informativePage.setTitle("test1");
		informativePage.setDefault(true);
		informativePage.setOwningYouthCouncil(youthCouncil);
		InformativePage informativePage2 = new InformativePage();
		informativePage2.setTitle("test1");
		informativePage2.setDefault(true);
		informativePage2.setOwningYouthCouncil(youthCouncil);

		informativePageRepository.save(informativePage);

		assertThrows(DataIntegrityViolationException.class, () -> informativePageRepository.save(informativePage2));


	}

	@Test
	void findInfoPageBlocksByTitle() {
		assertThrows(InformativePageNotFoundException.class, () -> informativePageService.findInfoPage("test", Optional.of("test")));
		assertThrows(InformativePageNotFoundException.class, () -> informativePageService.findDefaultInfoPage("test1"));
		//		assertEquals(2, informativePageService.findInfoPageBlocksByTitle("test", "Mortsel").size());
		//		assertTrue()
	}

	@Test
	void findInfoPageByTitle() {
		assertThrows(InformativePageNotFoundException.class, () -> informativePageService.findDefaultInfoPage("test1"));
	}
}
