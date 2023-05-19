package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.BlockDto;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePageBlock;
import be.kdg.youthcouncil.exceptions.InformativePageNotFoundException;
import be.kdg.youthcouncil.exceptions.InformativePageSetupMismatchException;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageBlockRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class InformativePageServiceImpl implements InformativePageService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final InformativePageRepository informativePageRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private final InformativePageBlockRepository informativePageBlockRepository;
	private ModelMapper modelMapper;


	@Override
	@Transactional
	public void save(String title, boolean isDefault, List<BlockDto> infoPageBlocksDto, Optional<String> municipality) {

		if (isDefault && municipality.isPresent()) {
			throw new InformativePageSetupMismatchException(municipality.get(), title);
		}

		InformativePage newInfoPage = municipality.map(m -> informativePageRepository.findByTitleAndMunicipality(title, m))
		                                          .orElseGet(() -> informativePageRepository.findDefaultByTitle(title))
		                                          .orElse(new InformativePage(
				                                          title,
				                                          isDefault,
				                                          municipality.map(m -> youthCouncilRepository.findByMunicipalityName(m)
				                                                                                      .orElseThrow(() -> new MunicipalityNotFoundException(m))
				                                          ).orElse(null)
		                                          ));


		informativePageBlockRepository.deleteAll(newInfoPage.getInfoPageBlocks());


		informativePageRepository.save(newInfoPage);

		newInfoPage.setInfoPageBlocks(infoPageBlocksDto.stream().map(blockDto -> {
			InformativePageBlock block = modelMapper.map(blockDto, InformativePageBlock.class);
			block.setOwningInformativePage(newInfoPage);
			informativePageBlockRepository.save(block);
			return block;
		}).toList());
	}


	@Override
	public InformativePage findInfoPage(String title, Optional<String> municipality) {
		logger.debug("im bugging");
		logger.debug(informativePageRepository.findAll().toString());
		return informativePageRepository.findByTitleAndMunicipality(title, municipality.orElse(null))
		                                .orElseThrow(() -> new InformativePageNotFoundException(title, municipality));
	}


	@Override
	public InformativePage findDefaultInfoPage(String title) {
		return informativePageRepository.findAllByTitle(title)
		                                .stream()
		                                .filter(InformativePage::isDefault)
		                                .findFirst()
		                                .orElseThrow(() -> new InformativePageNotFoundException(title));

	}

	@Override
	public List<BlockDto> findInfoPageBlocks(Optional<String> municipality, String title) {
		List<InformativePageBlock> blocks;
		if (municipality.isEmpty()) {
			blocks = informativePageRepository.findInfoPageBlocks(title);
		} else {
			blocks = informativePageRepository.findInfoPageBlocks(municipality.get(), title);
		}
		return blocks.stream()
		             .map(block -> modelMapper.map(block, BlockDto.class))
		             .toList();
	}

	@Override
	public boolean exists(Optional<String> municipality, String title) {
		return municipality.map(s -> informativePageRepository.findByTitleAndMunicipality(title, s).isPresent())
		                   .orElseGet(() -> informativePageRepository.findDefaultByTitle(title).isPresent());
	}
}
