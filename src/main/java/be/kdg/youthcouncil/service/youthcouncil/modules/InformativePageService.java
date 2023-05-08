package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.InformativePageBlockDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;

import java.util.List;
import java.util.Optional;


public interface InformativePageService {


	void save(String title, boolean isDefault, List<InformativePageBlockDto> infoPageBlocks, Optional<String> municipality);


	InformativePage findInfoPage(String title, Optional<String> municipality);

	InformativePage findDefaultInfoPage(String title);

	List<InformativePageBlockDto> findInfoPageBlocks(Optional<String> municipality, String title);

	boolean exists(Optional<String> municipality, String title);
}
