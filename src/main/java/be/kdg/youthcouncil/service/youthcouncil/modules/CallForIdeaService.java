package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CallForIdeaService {
	CallForIdea create(CallForIdeasDTO callForIdeasDTO, long youthCouncilId);

	CallForIdea findById(long id);

	CallForIdea save(CallForIdea callForIdea);

	void setDisplay(long actionPointId, boolean isDisplayed);


	CallForIdea findWithIdeas(long id);

	CallForIdea findByIdWithIdeasWithReactions(long id);


	boolean processCSVUpload(MultipartFile file, long callForIdeaId);

	List<CallForIdea> findAllByMunicipalityNameWithIdeas(String municipality);

	List<Theme> findAllThemes();
}
