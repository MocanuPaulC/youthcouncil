package be.kdg.youthcouncil.service.youthcouncil.modules;


import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditInfoPageBlockDto;

public interface BlockService {

	EditInfoPageBlockDto editBlock(EditInfoPageBlockDto editInfoPageBlockDto);

	BlockDto createBlock(BlockDto infoPageBlockDto, long infoPageId);

	void deleteBlock(long blockId);
}
