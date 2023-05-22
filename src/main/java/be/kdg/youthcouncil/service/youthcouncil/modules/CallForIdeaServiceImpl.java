package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ModuleStatus;
import be.kdg.youthcouncil.exceptions.CallForIdeaNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
	public CallForIdea findById(long id) {
		return callForIdeaRepository.findById(id).orElseThrow(() -> new CallForIdeaNotFoundException(id));
	}

	@Override
	public void setDisplay(long callForIdeaId, boolean isDisplayed) {
		CallForIdea callForIdea = callForIdeaRepository.findById(callForIdeaId)
		                                               .orElseThrow(() -> new CallForIdeaNotFoundException(callForIdeaId));
		if (isDisplayed) {
			callForIdea.setModuleStatus(ModuleStatus.DISPLAYED);
		} else {
			callForIdea.setModuleStatus(ModuleStatus.HIDDEN);
		}
		callForIdeaRepository.save(callForIdea);
	}

	public CallForIdea save(CallForIdea callForIdea) {
		return callForIdeaRepository.save(callForIdea);
	}

	@Override
	public CallForIdea findWithIdeas(long id) {
		return callForIdeaRepository.findWithIdeas(id).orElseThrow(() -> new CallForIdeaNotFoundException(id));
	}

	@Transactional (readOnly = true)
	@Override
	public CallForIdea findByIdWithIdeasWithReactions(long id) {
		CallForIdea callForIdea = callForIdeaRepository.findWithIdeas(id)
		                                               .orElseThrow(() -> new CallForIdeaNotFoundException(id));
		addIdeaReactions(callForIdea.getIdeas());
		return callForIdea;
	}

	@Override
	public List<CallForIdea> findAllByMunicipalityNameWithIdeas(String municipality) {
		List<CallForIdea> list = callForIdeaRepository.findAllByMunicipalityNameWithIdeas(municipality);
		logger.debug("list is here");
		logger.debug(list.toString());
		return list;
	}


	public void addIdeaReactions(List<Idea> ideas) {
		ideas.forEach(actionPoint -> addReactions(actionPoint, actionPoint.getReactions(), new ArrayList<IdeaReaction>()));
	}

	private void addReactions(Idea idea, List<IdeaReaction> reactions, List<IdeaReaction> reactionCopy) {
		idea.setReactions(reactionCopy);
		reactions.forEach(reaction -> addReaction(idea, reaction));
	}

	private void addReaction(Idea idea, IdeaReaction reaction) {
		idea.addReaction(reaction);
	}


}
