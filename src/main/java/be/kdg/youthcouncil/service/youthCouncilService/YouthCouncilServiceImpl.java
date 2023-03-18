package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.persistence.YouthCouncilRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class YouthCouncilServiceImpl implements YouthCouncilService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;


	@Override
	public List<User> getMembers(String municipality) {
		List<User> councilMembers = new ArrayList<>();
		try {
			councilMembers.addAll(
					youthCouncilRepository.
							findByMunicipalityNameWithCouncilMembers(municipality).
							getCouncilMembers());
			councilMembers.addAll(youthCouncilRepository.findByMunicipalityNameWithCouncilAdmins(municipality)
			                                            .getCouncilAdmins());
		} catch (NullPointerException e) {
			logger.debug("No members found for youth council of municipality: " + municipality);
		}
		return councilMembers;

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
	public List<ActionPoint> getFilteredActionPoints(String municipality, String theme, String label) {
		logger.debug("Getting filtered action points");
		//TODO: implement
		return null;
	}

	@Override
	public Optional<YouthCouncil> findByMunicipality(String municipality) {
		return Optional.of(youthCouncilRepository.findByMunicipalityName(municipality));
	}


	@Override
	public List<InformativePage> getAllInformativePages(String municipality) {
		logger.debug("Getting all Informative pages for youth council: " + municipality + "!");
		Optional<YouthCouncil> possibleYouthCouncil = youthCouncilRepository.getYouthCouncilWithInformativePages(municipality);
		if (possibleYouthCouncil.isEmpty())
			throw new MunicipalityNotFound("The youth-council for the municipality " + municipality + " could not be found.");
		return possibleYouthCouncil.get().getInformativePages();
	}
}
