package be.kdg.youthcouncil.service.informativePageService;

import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import be.kdg.youthcouncil.persistence.InformativePageRepository;
import be.kdg.youthcouncil.persistence.YouthCouncilRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class InformativePageServiceImpl implements InformativePageService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final InformativePageRepository informativePageRepository;
	private final YouthCouncilRepository youthCouncilRepository;

	@Override
	public List<InformativePage> getAll() {
		logger.debug("Getting all Informative Pages!");
		return informativePageRepository.findAll();
	}

}
