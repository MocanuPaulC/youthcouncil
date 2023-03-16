package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewInformativePageViewModel;
import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;

import java.util.List;
import java.util.Optional;

public interface YouthCouncilService {

	void create(NewYouthCouncilViewModel councilCreateModel);

	List<YouthCouncil> getAllYouthCouncils();

	List<ActionPoint> getFilteredActionPoints(String municipality, String theme, String label);

	YouthCouncil getYouthCouncil(long id);

	Optional<YouthCouncil> findByMunicipality(String municipality);

	void save(String municipality, NewInformativePageViewModel informativePage);


	List<InformativePage> getAllInformativePages(String municipality);
}
