package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.CallForIdeasDTO;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ModuleStatus;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import be.kdg.youthcouncil.exceptions.CallForIdeaNotFoundException;
import be.kdg.youthcouncil.exceptions.YouthCouncilIdNotFoundException;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.IdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CallForIdeaServiceImpl implements CallForIdeaService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ModelMapper modelMapper;
	private final CallForIdeaRepository callForIdeaRepository;
	private final IdeaRepository ideaRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private final ThemeRepository themeRepository;

	@Override
	public CallForIdea create(CallForIdeasDTO callForIdeasDTO, long youthCouncilId) {
		logger.debug("Creating call for ideas");
		CallForIdea callForIdea = modelMapper.map(callForIdeasDTO, CallForIdea.class);
		callForIdea.setOwningYouthCouncil(youthCouncilRepository.findById(youthCouncilId)
		                                                        .orElseThrow(() -> new YouthCouncilIdNotFoundException(youthCouncilId)));
		return callForIdeaRepository.save(callForIdea);
	}

	@Override
	public CallForIdea findById(long id) {
		return callForIdeaRepository.findById(id).orElseThrow(() -> new CallForIdeaNotFoundException(id));
	}

	@Override
	public void setDisplay(long callForIdeaId, boolean isDisplayed) {
		CallForIdea callForIdea = callForIdeaRepository.findById(callForIdeaId)
		                                               .orElseThrow(() -> new CallForIdeaNotFoundException(callForIdeaId));
		if (isDisplayed) {
			callForIdea.setModuleStatus(ModuleStatus.DISPLAYED);
		} else {
			callForIdea.setModuleStatus(ModuleStatus.HIDDEN);
		}
		callForIdeaRepository.save(callForIdea);
	}

	public CallForIdea save(CallForIdea callForIdea) {
		return callForIdeaRepository.save(callForIdea);
	}

	@Override
	public CallForIdea findWithIdeas(long id) {
		return callForIdeaRepository.findWithIdeas(id).orElseThrow(() -> new CallForIdeaNotFoundException(id));
	}

	@Transactional (readOnly = true)
	@Override
	public CallForIdea findByIdWithIdeasWithReactions(long id) {
		CallForIdea callForIdea = callForIdeaRepository.findWithIdeas(id)
		                                               .orElseThrow(() -> new CallForIdeaNotFoundException(id));
		addIdeaReactions(callForIdea.getIdeas());
		return callForIdea;
	}

	@Override
	public List<CallForIdea> findAllByMunicipalityNameWithIdeas(String municipality) {
		List<CallForIdea> list = callForIdeaRepository.findAllByMunicipalityNameWithIdeas(municipality);
		logger.debug("list is here");
		logger.debug(list.toString());
		return list;
	}

	@Override
	public List<Theme> findAllThemes() {
		return themeRepository.findAll();
	}

	@Override
	public boolean processCSVUpload(MultipartFile file, long callForIdeaId) {
		CallForIdea callForIdea = callForIdeaRepository.findWithIdeas(callForIdeaId)
		                                               .orElseThrow(() -> new CallForIdeaNotFoundException(callForIdeaId));
		return isCSVFile(file, callForIdea);

	}

	private boolean isCSVFile(MultipartFile file, CallForIdea callForIdea) {
		try (CSVParser csvParser = CSVFormat.DEFAULT.parse(new InputStreamReader(file.getInputStream()))) {
			// Iterate over the parsed CSV rows if needed
			List<CSVRecord> records = csvParser.getRecords();
			int size = records.size();
			records = records.subList(1, size);
			List<Idea> ideas = new ArrayList<>();
			List<SubTheme> subThemes = callForIdea.getTheme().getSubThemes();

			for (CSVRecord record : records) {
				SubTheme subTheme = subThemes
						.stream()
						.filter(st -> st.getSubTheme().equals(record.get(1)))
						.findFirst()
						.orElse(null);

				Idea idea = new Idea();
				idea.setCallForIdeas(callForIdea);
				idea.setIdea(record.get(2));
				idea.setFullName(record.get(0));
				idea.setSubTheme(subTheme);
				ideas.add(idea);
				callForIdea.addIdea(idea);
			}
			ideaRepository.saveAll(ideas);
			callForIdeaRepository.save(callForIdea);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}


	public void addIdeaReactions(List<Idea> ideas) {
		ideas.forEach(actionPoint -> addReactions(actionPoint, actionPoint.getReactions(), new ArrayList<IdeaReaction>()));
	}

	private void addReactions(Idea idea, List<IdeaReaction> reactions, List<IdeaReaction> reactionCopy) {
		idea.setReactions(reactionCopy);
		reactions.forEach(reaction -> addReaction(idea, reaction));
	}

	private void addReaction(Idea idea, IdeaReaction reaction) {
		idea.addReaction(reaction);
	}


}
