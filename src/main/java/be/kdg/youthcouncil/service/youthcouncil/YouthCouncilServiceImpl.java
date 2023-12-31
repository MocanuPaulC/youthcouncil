package be.kdg.youthcouncil.service.youthcouncil;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.interactions.ActionPointReactionRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class YouthCouncilServiceImpl implements YouthCouncilService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final YouthCouncilRepository youthCouncilRepository;
	private final YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	private final ActionPointService actionPointService;
	private final ActionPointReactionRepository actionPointReactionRepository;

	private final UserRepository userRepository;

	@Override
	public YouthCouncil findById(long id) {
		return youthCouncilRepository.findById(id)
		                             .orElseThrow(() -> new MunicipalityNotFoundException("The YouthCouncil " + id + "  could not be found!"));
	}

	@Override
	public List<PlatformUser> getAllMembers(String municipality) {
		return youthCouncilSubscriptionRepository.findSubscribersByMunicipality(municipality);
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
		                             .orElseThrow(() -> new MunicipalityNotFoundException("The youth-council for the municipality " + municipality + " could not be found."));
	}

	@Override
	public YouthCouncil findByIdWithMembers(long youthCouncilId) {
		return youthCouncilRepository.findWithSubscriptions(youthCouncilId)
		                             .orElseThrow(() -> new YouthCouncilSubscriptionNotFoundException(youthCouncilId));
	}

	@Override
	public List<InformativePage> getAllInformativePages(String municipality) {
		logger.debug("Getting all Informative pages for youth council: " + municipality + "!");
		Optional<YouthCouncil> possibleYouthCouncil = youthCouncilRepository.getWithInformativePages(municipality);
		if (possibleYouthCouncil.isEmpty())
			throw new MunicipalityNotFoundException("The youth-council for the municipality " + municipality + " could not be found.");
		return possibleYouthCouncil.get().getInformativePages();
	}

	@Override
	public YouthCouncil findByMunicipalityWithAnnouncements(String municipality) {
		return youthCouncilRepository.findWithAnnouncementsByMunicipality(municipality)
		                             .orElseThrow(() -> new MunicipalityNotFoundException("The youth-council for the municipality " + municipality + " could not be found."));
	}

	@Override
	public YouthCouncil findByMunicipalityWithCallsForIdeas(String municipality) {
		return youthCouncilRepository.findWithCallsForIdeasByMunicipality(municipality)
		                             .orElseThrow(() -> new MunicipalityNotFoundException("The youth-council for the municipality " + municipality + " could not be found."));
	}

	@Transactional (readOnly = true)
	@Override
	public YouthCouncil findByMunicipalityWithActionPoints(String municipality) {
		YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityWithActionPoints(municipality)
		                                                  .orElseThrow(() -> new MunicipalityNotFoundException("The youth-council for the municipality " + municipality + " could not be found."));
		addActionPointReactions(youthCouncil.getActionPoints());
		return youthCouncil;
	}


	@Transactional (readOnly = true)
	@Override
	public YouthCouncil findByMunicipalityWithActionPointsDisplayed(String municipality) {
		YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityWithActionPointsToDisplay(municipality)
		                                                  .orElseGet(() -> {
			                                                  YouthCouncil youthCouncil1 = youthCouncilRepository.findByMunicipalityName(municipality)
			                                                                                                     .orElseThrow(() -> new MunicipalityNotFoundException(municipality));
			                                                  youthCouncil1.setActionPoints(new ArrayList<>());
			                                                  return youthCouncil1;
		                                                  });

		addActionPointReactions(youthCouncil.getActionPoints());
		return youthCouncil;
	}


	@Override
	public YouthCouncil findByMunicipalityWithCallsForIdeasDisplayed(String municipality) {
		return youthCouncilRepository.findByMunicipalityWithCallForIdeasDisplayed(municipality)
		                             .orElseGet(() -> {
			                             YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityName(municipality)
			                                                                               .orElseThrow(() -> new MunicipalityNotFoundException(municipality));
			                             youthCouncil.setCallForIdeas(new ArrayList<>());
			                             return youthCouncil;
		                             });
	}

	@Override
	public YouthCouncil findByMunicipalityWithAnnouncementsDisplayed(String municipality) {
		return youthCouncilRepository.findByMunicipalityWithAnnouncementsDisplayed(municipality)
		                             .orElseGet(() -> {
			                             YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityName(municipality)
			                                                                               .orElseThrow(() -> new MunicipalityNotFoundException(municipality));
			                             youthCouncil.setAnnouncements(new ArrayList<>());
			                             return youthCouncil;
		                             });
	}

	public void addActionPointReactions(List<ActionPoint> actionPoints) {
		actionPoints.forEach(actionPoint -> addReactions(actionPoint, actionPoint.getReactions(), new ArrayList<ActionPointReaction>()));
	}

	private void addReactions(ActionPoint actionPoint, List<ActionPointReaction> reactions, List<ActionPointReaction> reactionCopy) {
		actionPoint.setReactions(reactionCopy);
		reactions.forEach(reaction -> addReaction(actionPoint, reaction));
	}

	private void addReaction(ActionPoint actionPoint, ActionPointReaction reaction) {
		actionPoint.addReaction(reaction);
	}

}

