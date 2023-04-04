package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewInformativePageViewModel;

public interface InformativePageService {

	void save(String municipality, NewInformativePageViewModel informativePage);

}
