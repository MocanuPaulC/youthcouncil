package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.persistence.YouthCouncilRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YouthCouncilServiceImpl implements YouthCouncilService {
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public YouthCouncilServiceImpl(ModelMapper modelMapper, YouthCouncilRepository youthCouncilRepository) {
		this.modelMapper = modelMapper;
		this.youthCouncilRepository = youthCouncilRepository;
	}

	@Override
	public void create(NewYouthCouncilViewModel councilCreateModel) {
		logger.debug("Saving youth council");
		youthCouncilRepository.save(modelMapper.map(councilCreateModel, YouthCouncil.class));
	}

	@Override
	public YouthCouncil getYouthCouncil(long id) {
		return youthCouncilRepository.findById(id).orElse(null);
	}

	@Override
	public List<YouthCouncil> getAllYouthCouncils() {
		logger.debug("Getting all youth councils");
		return youthCouncilRepository.findAll();
	}

	@Override
	public List<ActionPoint> getFilteredActionPoints(String municipalityName, String theme, String label) {
		logger.debug("Getting filtered action points");
		//TODO: implement
		return null;
	}

	@Override
	public Optional<YouthCouncil> findByMunicipality(String municipality) {
		return Optional.of(youthCouncilRepository.findByMunicipalityName(municipality));
	}
}
