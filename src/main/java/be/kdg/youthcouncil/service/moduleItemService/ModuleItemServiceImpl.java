package be.kdg.youthcouncil.service.moduleItemService;

import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import be.kdg.youthcouncil.persistence.ModuleItemRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ModuleItemServiceImpl implements ModuleItemService {
	private final ModelMapper modelMapper;
	private final ModuleItemRepository moduleItemRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<ModuleItem> getAllModuleItems() {
		logger.debug("Getting all module items");
		return moduleItemRepository.findAll();
	}

	@Override
	public void handleCallForIdeas(String idea, long moduleItemId, long userId, String theme) {
		logger.debug("Handling call for ideas");
		//TODO: implement
	}
}
