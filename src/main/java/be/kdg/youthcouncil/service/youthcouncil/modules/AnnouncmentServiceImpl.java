package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.mvc.viewModels.NewAnnoucementViewModel;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Announcement;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ModuleStatus;
import be.kdg.youthcouncil.exceptions.AnnouncementNotFoundException;
import be.kdg.youthcouncil.exceptions.MunicipalityNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.AnnouncementRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class AnnouncmentServiceImpl implements AnnouncementService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final AnnouncementRepository announcementRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private ModelMapper modelMapper;

	@Override
	public Announcement findByIdAndMunicipality(String municipality, long id) {
		return announcementRepository.findByIdAndMunicipality(municipality, id)
		                             .map(a -> a)
		                             .orElseThrow(() -> new AnnouncementNotFoundException(id));
	}

	@Override
	public List<Announcement> findAll() {
		return announcementRepository.findAll();
	}

	@Override
	public List<Announcement> findAll(String municipality) {
		return announcementRepository.findByMunicipality(municipality);
	}

	@Override
	public void setDisplay(long actionPointId, boolean isDisplayed) {
		Announcement announcement = announcementRepository.findById(actionPointId)
		                                                  .orElseThrow(() -> new AnnouncementNotFoundException(actionPointId));
		if (isDisplayed) {
			announcement.setModuleStatus(ModuleStatus.DISPLAYED);
		} else {
			announcement.setModuleStatus(ModuleStatus.HIDDEN);
		}
		announcementRepository.save(announcement);
	}

	@Transactional
	@Override
	public void save(String municipality, NewAnnoucementViewModel announcementViewModel) {
		YouthCouncil youthCouncil = youthCouncilRepository.findByMunicipalityName(municipality)
		                                                  .orElseThrow(() -> new MunicipalityNotFoundException("The municipality " + municipality + "could not be found!"));
		Announcement announcement = modelMapper.map(announcementViewModel, Announcement.class);
		announcement.setAnnouncementTime(LocalDateTime.now());
		announcement.setModuleStatus(ModuleStatus.DISPLAYED);
		youthCouncil.getAnnouncements().add(announcement); //FIXME please make me prettier
		announcement.setOwningYouthCouncil(youthCouncil);

		announcement = announcementRepository.save(announcement);
		logger.debug(announcement.toString());
	}
}
