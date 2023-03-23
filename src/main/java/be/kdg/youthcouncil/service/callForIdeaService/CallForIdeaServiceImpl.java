package be.kdg.youthcouncil.service.callForIdeaService;

import be.kdg.youthcouncil.controllers.api.dto.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.moduleItems.CallForIdea;
import be.kdg.youthcouncil.persistence.CallForIdeaRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CallForIdeaServiceImpl implements CallForIdeaService {

	private final CallForIdeaRepository callForIdeaRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;

	@Override
	public CallForIdea create(CallForIdeasDTO callForIdeasDTO) {
		logger.debug("Creating call for ideas");
		return callForIdeaRepository.save(modelMapper.map(callForIdeasDTO, CallForIdea.class));
	}

	@Override
	public CallForIdea find(long id) {
		return null;
	}

	@Override
	public CallForIdea save(CallForIdea callForIdea) {
		return null;
	}
}
