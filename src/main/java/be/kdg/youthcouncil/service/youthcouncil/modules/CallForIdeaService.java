package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;

public interface CallForIdeaService {
	CallForIdea create(CallForIdeasDTO callForIdeasDTO);

	CallForIdea findById(long id);

	CallForIdea save(CallForIdea callForIdea);

	void setDisplay(long actionPointId, boolean isDisplayed);


	CallForIdea findWithIdeas(long id);

	CallForIdea findByIdWithIdeasWithReactions(long id);
}
