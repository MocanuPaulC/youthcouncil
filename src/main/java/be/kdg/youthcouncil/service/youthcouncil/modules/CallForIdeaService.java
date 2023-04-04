package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;

public interface CallForIdeaService {
	CallForIdea create(CallForIdeasDTO callForIdeasDTO);

	CallForIdea find(long id);

	CallForIdea save(CallForIdea callForIdea);

	CallForIdea findWithIdeas(long id);
}
