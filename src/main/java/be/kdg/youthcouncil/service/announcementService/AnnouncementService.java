package be.kdg.youthcouncil.service.announcementService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewAnnoucementViewModel;
import be.kdg.youthcouncil.domain.moduleItems.Announcement;

import java.util.List;

public interface AnnouncementService {

	List<Announcement> findAll();

	List<Announcement> findAll(String municipality);

	void save(String municipality, NewAnnoucementViewModel announcement);
}
