package be.kdg.youthcouncil.controllers.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping
	public String index() {
		logger.debug("Running index controller");
		return "index";
	}
}
