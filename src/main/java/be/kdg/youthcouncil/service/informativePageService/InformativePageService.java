package be.kdg.youthcouncil.service.informativePageService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewInformativePageViewModel;

public interface InformativePageService {

	void save(String municipality, NewInformativePageViewModel informativePage);

}
