package be.kdg.youthcouncil.service.youthcouncil.modules;


import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditInfoPageBlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.InformativePageBlockDto;

public interface BlockService {

	EditInfoPageBlockDto editBlock(EditInfoPageBlockDto editInfoPageBlockDto);

	InformativePageBlockDto createBlock(InformativePageBlockDto infoPageBlockDto, long infoPageId);

	void deleteBlock(long blockId);
}
