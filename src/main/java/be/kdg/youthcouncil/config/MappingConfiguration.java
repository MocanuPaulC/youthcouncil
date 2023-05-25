package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.interactions.ReactionDto;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules.*;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.NewSubscriptionDTO;
import be.kdg.youthcouncil.controllers.api.dto.youthcouncil.subscriptions.SubscriptionDTO;
import be.kdg.youthcouncil.controllers.mvc.viewModels.NewYouthCouncilViewModel;
import be.kdg.youthcouncil.domain.Municipality;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.modules.*;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.*;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.MunicipalityRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.ActionPointRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.CallForIdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.IdeaRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
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
	private final IdeaRepository ideaRepository;
	private final MunicipalityRepository municipalityRepository;

	private final ThemeRepository themeRepository;

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		converterConfiguration(modelMapper);

		modelMapper.createTypeMap(NewIdeaDTO.class, Idea.class)
		           .addMapping(NewIdeaDTO::getUserId, Idea::setUser)
		           .addMapping(NewIdeaDTO::getCallForIdeaId, Idea::setCallForIdeas)
		           .addMapping(NewIdeaDTO::getSubThemeId, Idea::setSubTheme)
		           .addMapping(NewIdeaDTO::getIdea, Idea::setIdea);
		modelMapper.createTypeMap(NewYouthCouncilViewModel.class, YouthCouncil.class)
		           .addMapping(NewYouthCouncilViewModel::getCouncilName, YouthCouncil::setCouncilName)
		           .addMapping(NewYouthCouncilViewModel::getMunicipality, YouthCouncil::setMunicipality);

		modelMapper.createTypeMap(Idea.class, IdeaDTO.class)
		           .addMapping(idea -> idea.getImage().getPath(), IdeaDTO::setImagePath)
		           .addMapping(idea -> idea.getUser().getUsername(), IdeaDTO::setUsername)
		           .addMapping(Idea::getIdeaId, IdeaDTO::setIdeaId)
		           .addMapping(Idea::getSubTheme, IdeaDTO::setSubTheme);

		modelMapper.createTypeMap(NewSubscriptionDTO.class, YouthCouncilSubscription.class)
		           .addMapping(NewSubscriptionDTO::getUserId, YouthCouncilSubscription::setSubscriber)
		           .addMapping(NewSubscriptionDTO::getYouthCouncilId, YouthCouncilSubscription::setYouthCouncil)
		           .addMapping(NewSubscriptionDTO::getRole, YouthCouncilSubscription::setRole);

		modelMapper.createTypeMap(YouthCouncilSubscription.class, SubscriptionDTO.class)
		           .addMapping(YouthCouncilSubscription::getRole, SubscriptionDTO::setSubscriptionRole)
		           .addMapping(YouthCouncilSubscription::getSubscriber, SubscriptionDTO::setSubscriberId)
		           .addMapping(YouthCouncilSubscription::getYouthCouncil, SubscriptionDTO::setYouthCouncilId)
		           .addMapping(YouthCouncilSubscription::getYouthCouncil, SubscriptionDTO::setYouthCouncilName);
		modelMapper.createTypeMap(ActionPoint.class, EditActionPointDto.class)
		           .addMapping(ActionPoint::getActionPointId, EditActionPointDto::setId)
		           .addMapping(ActionPoint::getTitle, EditActionPointDto::setTitle)
		           .addMapping(ActionPoint::getDescription, EditActionPointDto::setDescription)
		           .addMapping(ActionPoint::getStatus, EditActionPointDto::setStatus);

		modelMapper.createTypeMap(ActionPointReaction.class, ReactionDto.class)
		           .addMapping(ActionPointReaction::getReaction, ReactionDto::setReaction)
		           .addMapping(ActionPointReaction::getActionPointReactedOn, ReactionDto::setEntityReactedOnId)
		           .addMapping(ActionPointReaction::getReactingUser, ReactionDto::setReactingUserId)
		           .addMapping(ActionPointReaction::getReactionId, ReactionDto::setReactionId);
		modelMapper.createTypeMap(IdeaReaction.class, ReactionDto.class)
		           .addMapping(IdeaReaction::getReaction, ReactionDto::setReaction)
		           .addMapping(IdeaReaction::getIdeaReactedOn, ReactionDto::setEntityReactedOnId)
		           .addMapping(IdeaReaction::getReactingUser, ReactionDto::setReactingUserId)
		           .addMapping(IdeaReaction::getReactionId, ReactionDto::setReactionId);
		modelMapper.createTypeMap(BlockDto.class, InformativePageBlock.class)
		           .addMapping(BlockDto::getType, InformativePageBlock::setType)
		           .addMapping(BlockDto::getContent, InformativePageBlock::setContent)
		           .addMapping(BlockDto::getOrderNumber, InformativePageBlock::setOrderNumber);
		modelMapper.createTypeMap(BlockDto.class, ActionPointBlock.class)
		           .addMapping(BlockDto::getType, ActionPointBlock::setType)
		           .addMapping(BlockDto::getContent, ActionPointBlock::setContent)
		           .addMapping(BlockDto::getOrderNumber, ActionPointBlock::setOrderNumber);

		modelMapper.createTypeMap(CallForIdeasDTO.class, CallForIdea.class)
		           .addMapping(CallForIdeasDTO::getTitle, CallForIdea::setTitle)
		           .addMapping(CallForIdeasDTO::getTheme, CallForIdea::setTheme);

		return modelMapper;
	}

	private void converterConfiguration(ModelMapper modelMapper) {
		Converter<Long, PlatformUser> toPlatformUser = new AbstractConverter<Long, PlatformUser>() {
			protected PlatformUser convert(Long source) {
				return userRepository.findById(source)
				                     .orElseThrow(() -> new UserNotFoundException("The user could not be found!"));
			}
		};
		modelMapper.addConverter(toPlatformUser);

		Converter<ActionPoint, Long> toActionPointId = new AbstractConverter<ActionPoint, Long>() {
			protected Long convert(ActionPoint source) {
				return source.getActionPointId();
			}
		};

		Converter<Idea, Long> toIdeaId = new AbstractConverter<Idea, Long>() {
			protected Long convert(Idea source) {
				return source.getIdeaId();
			}
		};
		modelMapper.addConverter(toIdeaId);
		modelMapper.addConverter(toActionPointId);

		Converter<Long, ActionPoint> toActionPoint = new AbstractConverter<Long, ActionPoint>() {
			protected ActionPoint convert(Long source) {
				return actionPointRepository.findById(source)
				                            .orElseThrow(() -> new ActionPointNotFoundException(source));
			}
		};

		Converter<String, Municipality> toMunicipality = new AbstractConverter<String, Municipality>() {
			protected Municipality convert(String source) {
				return municipalityRepository.findByName(source)
				                             .orElseThrow(() -> new MunicipalityNotFoundException(source));
			}
		};
		modelMapper.addConverter(toMunicipality);


		Converter<Long, Idea> toIdea = new AbstractConverter<Long, Idea>() {
			protected Idea convert(Long source) {
				return ideaRepository.findById(source).orElseThrow(() -> new IdeaNotFoundException(source));
			}
		};
		modelMapper.addConverter(toActionPoint);
		Converter<Long, CallForIdea> toCallForIdeas = new AbstractConverter<Long, CallForIdea>() {
			protected CallForIdea convert(Long source) {
				return callForIdeaRepository.findById(source)
				                            .orElseThrow(() -> new CallForIdeaNotFoundException(source));
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
				return youthCouncilRepository.findById(source)
				                             .orElseThrow(() -> new YouthCouncilIdNotFoundException(source));
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

		Converter<Long, Theme> fromLongToTheme = new AbstractConverter<Long, Theme>() {
			@Override
			protected Theme convert(Long source) {
				return themeRepository.findById(source).orElseThrow(() -> new ThemeNotFoundException(source));
			}
		};
		modelMapper.addConverter(fromLongToTheme);

		modelMapper.addMappings(new PropertyMap<ReactionDto, ActionPointReaction>() {

			@Override
			protected void configure() {
				using(toPlatformUser).map(source.getReactingUserId(), destination.getReactingUser());
				using(toActionPoint).map(source.getEntityReactedOnId(), destination.getActionPointReactedOn());
				map().setReaction(source.getReaction());
				map().setReactionId(source.getReactionId());
			}
		});
		modelMapper.addMappings(new PropertyMap<ReactionDto, IdeaReaction>() {

			@Override
			protected void configure() {
				using(toPlatformUser).map(source.getReactingUserId(), destination.getReactingUser());
				using(toIdea).map(source.getEntityReactedOnId(), destination.getIdeaReactedOn());
				map().setReaction(source.getReaction());
				map().setReactionId(source.getReactionId());
			}
		});

		Converter<SubTheme, String> fromSubThemeToString = new AbstractConverter<SubTheme, String>() {
			@Override
			protected String convert(SubTheme source) {
				return source.getSubTheme();
			}
		};

		modelMapper.addConverter(fromSubThemeToString);

	}


}
