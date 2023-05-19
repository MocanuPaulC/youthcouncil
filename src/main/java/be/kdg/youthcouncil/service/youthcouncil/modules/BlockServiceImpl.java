package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.EditInfoPageBlockDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePageBlock;
import be.kdg.youthcouncil.exceptions.InformativePageBlockNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageBlockRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlockServiceImpl implements BlockService {

	private final InformativePageBlockRepository informativePageBlockRepository;
	private final ModelMapper modelMapper;
	private final InformativePageRepository informativePageRepository;

	@Override
	public EditInfoPageBlockDto editBlock(EditInfoPageBlockDto editInfoPageBlockDto) {
		InformativePageBlock oldBlock = informativePageBlockRepository.findById(editInfoPageBlockDto.getBlockId())
		                                                              .orElseThrow(() -> new InformativePageBlockNotFoundException(editInfoPageBlockDto.getBlockId()));
		InformativePageBlock editedBlock = modelMapper.map(editInfoPageBlockDto, InformativePageBlock.class);
		if (editedBlock.equals(oldBlock)) {
			return editInfoPageBlockDto;
		}
		oldBlock.setType(editedBlock.getType());
		oldBlock.setContent(editedBlock.getContent());
		oldBlock.setOrderNumber(editedBlock.getOrderNumber());

		return modelMapper.map(informativePageBlockRepository.save(oldBlock), EditInfoPageBlockDto.class);
	}

	@Override
	public BlockDto createBlock(BlockDto infoPageBlockDto, long infoPageId) {
		InformativePage infoPage = informativePageRepository.findById(infoPageId)
		                                                    .orElseThrow(() -> new InformativePageBlockNotFoundException(infoPageId));
		InformativePageBlock newBlock = modelMapper.map(infoPageBlockDto, InformativePageBlock.class);
		informativePageBlockRepository.save(newBlock);
		infoPage.addBlock(newBlock);
		informativePageRepository.save(infoPage);
		return modelMapper.map(newBlock, BlockDto.class);
	}

	@Override
	public void deleteBlock(long blockId) {
		informativePageBlockRepository.deleteById(blockId);
	}


}
