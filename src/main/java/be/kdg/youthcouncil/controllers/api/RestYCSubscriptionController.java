package be.kdg.youthcouncil.controllers.api;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.NewSubscriptionDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.SubscriptionDTO;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping ("/api/ycsubscriptions")
public class RestYCSubscriptionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ModelMapper modelMapper;
	private final YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	private final YouthCouncilSubscriptionService youthCouncilSubscriptionService;

	@PostMapping ()
	public SubscriptionDTO addSubscription(@Valid @RequestBody NewSubscriptionDTO subscriptionDTO) {
		//TODO right now whatever subscription is passed here will just be made into the role of the user
		// maybe this should first check if the user has permissions to change that or just set user by default
		// and have a different path for creating council admins and or creating moderators
		YouthCouncilSubscription youthCouncilSubscription = youthCouncilSubscriptionService.create(modelMapper.map(subscriptionDTO, YouthCouncilSubscription.class));
		return modelMapper.map(youthCouncilSubscription, SubscriptionDTO.class);
	}

}
