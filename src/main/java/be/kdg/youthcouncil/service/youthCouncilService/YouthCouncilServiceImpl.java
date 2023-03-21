package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFound;
import be.kdg.youthcouncil.persistence.UserRepository;
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

	private final UserRepository userRepository;

	@Override
	public YouthCouncil findById(long id) {
		return youthCouncilRepository.findById(id)
		                             .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + id + "  could not be found!"));
	}

	@Override
	public List<User> getAllMembers(String municipality) {
		List<User> membersToReturn = new ArrayList<>();
		try {
			List<User> allUsers = userRepository.findAllWithIdeas();
			List<User> councilMembers =
					youthCouncilRepository.findByMunicipalityNameWithCouncilMembers(municipality)
					                      .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + municipality + "  could not be found!"))
					                      .getCouncilMembers();
			List<User> councilAdmins =
					youthCouncilRepository.findByMunicipalityNameWithCouncilAdmins(municipality)
					                      .orElseThrow(() -> new MunicipalityNotFound("The YouthCouncil " + municipality + "  could not be found!"))
					                      .getCouncilAdmins();

			for (User user : allUsers) {

				if (councilMembers.contains(user)) {
					membersToReturn.add(user);
				}
				if (councilAdmins.contains(user)) {
					membersToReturn.add(user);
				}
			}
		} catch (NullPointerException e) {
			logger.debug("No members found for youth council of municipality: " + municipality);
		}
		return membersToReturn;

	}

	@Override
	public void save(YouthCouncil youthCouncil) {
		youthCouncilRepository.save(youthCouncil);
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
	public List<YouthCouncil> findAllYouthCouncils() {
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
	public YouthCouncil findByMunicipality(String municipality) {
		return youthCouncilRepository.findByMunicipalityName(municipality)
		                             .orElseThrow(() -> new MunicipalityNotFound("The youth-council for the municipality " + municipality + " could not be found."));

	}

	@Override
	public YouthCouncil findByIdWithMembers(long youthCouncilId) {
		return youthCouncilRepository.findByIdWithCouncilMembers(youthCouncilId)
		                             .orElseThrow(() -> new MunicipalityNotFound("The youth-council for the municipality " + youthCouncilId + " could not be found."));
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
