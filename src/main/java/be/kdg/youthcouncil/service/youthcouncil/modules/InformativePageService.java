package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;

import java.util.List;
import java.util.Optional;


public interface InformativePageService {


	void save(String title, boolean isDefault, List<BlockDto> infoPageBlocks, Optional<String> municipality);

	List<InformativePage> findAllByMunicipalityName(String municipality);

	InformativePage findInfoPage(String title, Optional<String> municipality);

	InformativePage findDefaultInfoPage(String title);

	List<BlockDto> findInfoPageBlocks(Optional<String> municipality, String title);

	boolean exists(Optional<String> municipality, String title);
}
