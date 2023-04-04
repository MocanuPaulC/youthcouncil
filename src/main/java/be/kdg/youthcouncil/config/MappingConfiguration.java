package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.IdeaDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.NewIdeaDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.NewSubscriptionDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.SubscriptionDTO;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.*;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MappingConfiguration {

	private final UserRepository userRepository;
	private final CallForIdeaRepository callForIdeaRepository;
	private final SubThemeRepository subThemeRepository;
	private final YouthCouncilRepository youthCouncilRepository;

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		converterConfiguration(modelMapper);

		modelMapper.createTypeMap(NewIdeaDTO.class, Idea.class)
		           .addMapping(NewIdeaDTO::getUserId, Idea::setUser)
		           .addMapping(NewIdeaDTO::getCallForIdeasId, Idea::setCallForIdeas)
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
		Converter<Long, PlatformUser> toPlatformUser = new AbstractConverter<Long, PlatformUser>() {
			protected PlatformUser convert(Long source) {
				return userRepository.findById(source).orElseThrow(() -> new UserNotFoundException("The user could not be found!"));
			}
		};
		modelMapper.addConverter(toPlatformUser);

		Converter<Long, CallForIdea> toCallForIdeas = new AbstractConverter<Long, CallForIdea>() {
			protected CallForIdea convert(Long source) {
				return callForIdeaRepository.findById(source).orElseThrow(() -> new CallForIdeaNotFoundException(source));
			}
		};
		modelMapper.addConverter(toCallForIdeas);

		Converter<Long, SubTheme> toSubTheme = new AbstractConverter<Long, SubTheme>() {
			protected SubTheme convert(Long source) {
				return subThemeRepository.findById(source).orElseThrow(() -> new SubThemeNotFoundException(source));
			}
		};
		modelMapper.addConverter(toSubTheme);

		Converter<Long, YouthCouncil> toYouthCouncil = new AbstractConverter<Long, YouthCouncil>() {
			protected YouthCouncil convert(Long source) {
				return youthCouncilRepository.findById(source).orElseThrow(() -> new YouthCouncilIdNotFoundException(source));
			}
		};
		modelMapper.addConverter(toYouthCouncil);

		Converter<Integer, SubscriptionRole> toSubscriptionRole = new AbstractConverter<Integer, SubscriptionRole>() {
			protected SubscriptionRole convert(Integer source) {
				try {
					return SubscriptionRole.values()[source];
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new InvalidSubscriptionIdException(source);
				}
			}
		};
		modelMapper.addConverter(toSubscriptionRole);

		Converter<SubscriptionRole, String> fromSubscriptionRoleToString = new AbstractConverter<SubscriptionRole, String>() {
			protected String convert(SubscriptionRole source) {
				return source != null ? source.toString() : "";
			}
		};
		modelMapper.addConverter(fromSubscriptionRoleToString);

		Converter<PlatformUser, Long> fromUserToLong = new AbstractConverter<PlatformUser, Long>() {
			protected Long convert(PlatformUser source) {
				return source != null ? source.getId() : null;
			}
		};
		modelMapper.addConverter(fromUserToLong);

		Converter<YouthCouncil, Long> fromYouthCouncilToLong = new AbstractConverter<YouthCouncil, Long>() {
			protected Long convert(YouthCouncil source) {
				return source != null ? source.getYouthCouncilId() : null;
			}
		};
		modelMapper.addConverter(fromYouthCouncilToLong);

		Converter<YouthCouncil, String> fromYouthCouncilToString = new AbstractConverter<YouthCouncil, String>() {
			protected String convert(YouthCouncil source) {
				return source != null ? source.getCouncilName() : "";
			}
		};
		modelMapper.addConverter(fromYouthCouncilToString);
	}


}
