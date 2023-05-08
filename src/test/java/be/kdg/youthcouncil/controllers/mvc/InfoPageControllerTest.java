package be.kdg.youthcouncil.controllers.mvc;

import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageBlockRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance (TestInstance.Lifecycle.PER_CLASS)
class InfoPageControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InformativePageBlockRepository blockRepository;

	@Autowired
	private InformativePageRepository informativePageRepository;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void createYouthCouncilInfoPage() {
	}

	@Test
	void createDefaultInfoPage() throws Exception {


		mockMvc.perform(post("/youthcouncils/infoPages/create/{title}", "test")
				       .contentType(MediaType.APPLICATION_JSON)
				       .content("""
						        [
						        {  "orderNumber" : 1,
						         "content" : "test",
						         "type" : "HEADER_BIG"
						       },
						       {  "orderNumber" : 2,
						         "content" : "testParagraph",
						         "type" : "PARAGRAPH"
						       }
						       ]"""))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(view().name("redirect:/"));

		assertEquals(blockRepository.findAll().size(), 2);
		assertEquals(informativePageRepository.findAll().size(), 5);
	}
}