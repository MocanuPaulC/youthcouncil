package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.IdeaRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class IdeaServiceImpl implements IdeaService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IdeaRepository ideaRepository;

	@Override
	public Idea createIdea(Idea idea) {
		logger.debug("Adding a new Idea!");
		return ideaRepository.save(idea);
	}
}
