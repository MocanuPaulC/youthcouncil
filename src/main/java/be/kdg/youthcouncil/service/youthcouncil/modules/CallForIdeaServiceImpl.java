package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.exceptions.CallForIdeaNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
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
		return callForIdeaRepository.findById(id)
		                            .orElseThrow(() -> {throw new CallForIdeaNotFoundException(id);});
	}

	public CallForIdea save(CallForIdea callForIdea) {
		return callForIdeaRepository.save(callForIdea);
	}

	@Override
	public CallForIdea findWithIdeas(long id) {
		return callForIdeaRepository.findWithIdeas(id).orElseThrow(() -> {throw new CallForIdeaNotFoundException(id);});
	}


}
