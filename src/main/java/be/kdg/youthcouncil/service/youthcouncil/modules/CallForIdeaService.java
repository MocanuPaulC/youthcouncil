package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CallForIdeaService {
	CallForIdea create(CallForIdeasDTO callForIdeasDTO);

	CallForIdea findById(long id);

	CallForIdea save(CallForIdea callForIdea);

	void setDisplay(long actionPointId, boolean isDisplayed);


	CallForIdea findWithIdeas(long id);

	CallForIdea findByIdWithIdeasWithReactions(long id);

	List<CallForIdea> findAllByMunicipalityNameWithIdeas(String municipality);

	boolean processCSVUpload(MultipartFile file, long callForIdeaId);
}
