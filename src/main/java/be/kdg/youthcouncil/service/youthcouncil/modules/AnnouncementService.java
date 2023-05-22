package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewAnnoucementViewModel;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Announcement;

import java.util.List;

public interface AnnouncementService {

	Announcement findByIdAndMunicipality(String municipality, long id);

	List<Announcement> findAll();

	List<Announcement> findAll(String municipality);

	void setDisplay(long actionPointId, boolean isDisplayed);

	void save(String municipality, NewAnnoucementViewModel announcement);
}
