package be.kdg.youthcouncil.service.callForIdeaService;

import be.kdg.youthcouncil.controllers.api.dto.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.moduleItems.CallForIdea;

public interface CallForIdeaService {
	CallForIdea create(CallForIdeasDTO callForIdeasDTO);

	CallForIdea find(long id);

	CallForIdea save(CallForIdea callForIdea);
}
