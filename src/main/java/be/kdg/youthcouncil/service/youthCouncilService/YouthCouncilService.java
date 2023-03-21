package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;

import java.util.List;

public interface YouthCouncilService {

	void create(NewYouthCouncilViewModel councilCreateModel);

	List<YouthCouncil> findAllYouthCouncils();

	List<ActionPoint> getFilteredActionPoints(String municipality, String theme, String label);


	List<User> getAllMembers(String municipality);

	void save(YouthCouncil youthCouncil);

	YouthCouncil findByIdWithMembers(long id);

	YouthCouncil findById(long id);


	YouthCouncil getYouthCouncil(long id);

	YouthCouncil findByMunicipality(String municipality);


	List<InformativePage> getAllInformativePages(String municipality);
}
