package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions.ActionPointReactionDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.IdeaDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.NewIdeaDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.NewSubscriptionDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.SubscriptionDTO;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.*;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MappingConfiguration {

	private final UserRepository userRepository;
	private final CallForIdeaRepository callForIdeaRepository;
	private final SubThemeRepository subThemeRepository;
	private final YouthCouncilRepository youthCouncilRepository;
	private final ActionPointRepository actionPointRepository;

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		converterConfiguration(modelMapper);

		modelMapper.createTypeMap(NewIdeaDTO.class, Idea.class)
		           .addMapping(NewIdeaDTO::getUserId, Idea::setUser)
		           .addMapping(NewIdeaDTO::getCallForIdeaId, Idea::setCallForIdeas)
		           .addMapping(NewIdeaDTO::getSubThemeId, Idea::setSubTheme);

		modelMapper.createTypeMap(Idea.class, IdeaDTO.class)
		           .addMapping(idea -> idea.getImage().getPath(), IdeaDTO::setImagePath)
		           .addMapping(idea -> idea.getUser().getUsername(), IdeaDTO::setUsername);

		modelMapper.createTypeMap(NewSubscriptionDTO.class, YouthCouncilSubscription.class)
		           .addMapping(NewSubscriptionDTO::getUserId, YouthCouncilSubscription::setSubscriber)
		           .addMapping(NewSubscriptionDTO::getYouthCouncilId, YouthCouncilSubscription::setYouthCouncil)
		           .addMapping(NewSubscriptionDTO::getRole, YouthCouncilSubscription::setRole);

		modelMapper.createTypeMap(YouthCouncilSubscription.class, SubscriptionDTO.class)
		           .addMapping(YouthCouncilSubscription::getRole, SubscriptionDTO::setSubscriptionRole)
		           .addMapping(YouthCouncilSubscription::getSubscriber, SubscriptionDTO::setSubscriberId)
		           .addMapping(YouthCouncilSubscription::getYouthCouncil, SubscriptionDTO::setYouthCouncilId)
		           .addMapping(YouthCouncilSubscription::getYouthCouncil, SubscriptionDTO::setYouthCouncilName);
		return modelMapper;
	}

	private void converterConfiguration(ModelMapper modelMapper) {
		Converter<Long, PlatformUser> toPlatformUser = context ->
				userRepository.findById(context.getSource())
				              .orElseThrow(() -> new UserNotFoundException("The user could not be found!"));
		modelMapper.addConverter(toPlatformUser);

		Converter<Long, ActionPoint> toActionPoint = context ->
				actionPointRepository.findById(context.getSource())
				                     .orElseThrow(() -> new ActionPointNotFoundException(context.getSource()));
		modelMapper.addConverter(toActionPoint);
		Converter<Long, CallForIdea> toCallForIdeas = context ->
				callForIdeaRepository.findById(context.getSource())
				                     .orElseThrow(() -> new CallForIdeaNotFoundException(context.getSource()));
		modelMapper.addConverter(toCallForIdeas);

		Converter<Long, SubTheme> toSubTheme = context ->
				subThemeRepository.findById(context.getSource())
				                  .orElseThrow(() -> new SubThemeNotFoundException(context.getSource()));
		modelMapper.addConverter(toSubTheme);

		Converter<Long, YouthCouncil> toYouthCouncil = context ->
				youthCouncilRepository.findById(context.getSource())
				                      .orElseThrow(() -> new YouthCouncilIdNotFoundException(context.getSource()));
		modelMapper.addConverter(toYouthCouncil);

		Converter<Integer, SubscriptionRole> toSubscriptionRole = context -> {
			try {
				return SubscriptionRole.values()[context.getSource()];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new InvalidSubscriptionIdException(context.getSource());
			}
		};
		modelMapper.addConverter(toSubscriptionRole);

		Converter<SubscriptionRole, String> fromSubscriptionRoleToString = context ->
				context.getSource() != null ? context.getSource().toString() : "";
		modelMapper.addConverter(fromSubscriptionRoleToString);

		Converter<PlatformUser, Long> fromUserToLong = context ->
				context.getSource() != null ? context.getSource().getId() : null;
		modelMapper.addConverter(fromUserToLong);

		Converter<YouthCouncil, Long> fromYouthCouncilToLong = context ->
				context.getSource() != null ? context.getSource().getYouthCouncilId() : null;
		modelMapper.addConverter(fromYouthCouncilToLong);

		Converter<YouthCouncil, String> fromYouthCouncilToString = context ->
				context.getSource() != null ? context.getSource().getCouncilName() : "";
		modelMapper.addConverter(fromYouthCouncilToString);

		modelMapper.addMappings(new PropertyMap<ActionPointReactionDto, ActionPointReaction>() {

			@Override
			protected void configure() {
				using(toPlatformUser).map(source.getReactingUserId(), destination.getReactingUser());
				using(toActionPoint).map(source.getActionPointReactedOnId(), destination.getActionPointReactedOn());
				map().setReaction(source.getReaction());
				skip(destination.getReactionId());
			}
		});
		modelMapper.addConverter(toPlatformUser);

	}


}
