package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewInformativePageViewModel;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.InformativePageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@AllArgsConstructor
@Service
public class InformativePageServiceImpl implements InformativePageService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final InformativePageRepository informativePageRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public void save(String municipality, NewInformativePageViewModel informativePageVm) {
		InformativePage infoPage = modelMapper.map(informativePageVm, InformativePage.class);
		YouthCouncil youthCouncil = youthCouncilRepository.getWithInformativePages(municipality)
		                                                  .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil could not be found!"));

		infoPage.setOwningYouthCouncil(youthCouncil);
		youthCouncil.addInformativePage(infoPage);


		InformativePage page = informativePageRepository.save(infoPage);
		logger.debug(page.toString());
	}
}
