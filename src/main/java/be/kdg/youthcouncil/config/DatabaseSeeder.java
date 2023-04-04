package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.users.GeneralAdmin;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.*;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.persistence.media.MediaRepository;
import be.kdg.youthcouncil.persistence.users.AdminRepository;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.*;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.ActionPointSubscriptionRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Profile ({"dev", "prod"})
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

	BCryptPasswordEncoder encoder;
	UserRepository userRepository;
	AdminRepository adminRepository;
	MediaRepository mediaRepository;
	YouthCouncilRepository youthCouncilRepository;
	AnnouncementRepository announcementRepository;
	CallForIdeaRepository callForIdeaRepository;
	IdeaRepository ideaRepository;
	ThemeRepository themeRepository;
	SubThemeRepository subThemeRepository;
	InformativePageRepository informativePageRepository;
	ActionPointRepository actionPointRepository;
	YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	ActionPointSubscriptionRepository actionPointSubscriptionRepository;

	@Override
	public void run(String... args) throws Exception {
		loadData();
	}

	private void loadData() {

		// USERS
		adminRepository.save(new GeneralAdmin("gadmin", encoder.encode("gadmin")));

		List<String> userNames = List.of("cadmin1", "cadmin2", "moderator1", "moderator2", "member1", "member2", "member3");
		List<Integer> userPostcodes = List.of(23423, 457456, 8325, 57809, 12362, 6799, 21356);
		List<PlatformUser> userList = new ArrayList<>();

		IntStream.range(0, 7).forEach(i -> {
			String n = userNames.get(i);
			userList.add(new PlatformUser(n, n, n + "@localhost", userPostcodes.get(0)
			                                                                   .toString(), n, encoder.encode(n)));
		});

		userRepository.saveAll(userList);

		//THEMES

		Theme theme1 = new Theme("a theme");
		Theme theme2 = new Theme("b theme");
		Theme theme3 = new Theme("c theme");

		themeRepository.saveAll(List.of(theme1, theme2, theme3));

		SubTheme subTheme1a = new SubTheme("a theme subtheme 1", theme1);
		SubTheme subTheme2a = new SubTheme("a theme subtheme 2", theme1);
		SubTheme subTheme1b = new SubTheme("b theme subtheme 1", theme2);
		SubTheme subTheme2b = new SubTheme("b theme subtheme 2", theme2);
		SubTheme subTheme1c = new SubTheme("c theme subtheme 1", theme3);
		SubTheme subTheme2c = new SubTheme("c theme subtheme 2", theme3);

		subThemeRepository.saveAll(List.of(subTheme1b, subTheme1a, subTheme1c, subTheme2c, subTheme2b, subTheme2a));

		//MEDIA
		Image councilLogo1 = new Image("my/council/logo1.jpg");
		Image councilLogo2 = new Image("my/council/logo2.jpg");
		Image image1 = new Image("random/image/1.jpg");
		Image image2 = new Image("random/image/2.jpg");
		Image image3 = new Image("random/image/3.jpg");
		Image image4 = new Image("random/image/4.jpg");
		Image image5 = new Image("random/image/5.jpg");
		Image image6 = new Image("random/image/6.jpg");
		Image image7 = new Image("random/image/7.jpg");

		mediaRepository.saveAll(List.of(councilLogo1, councilLogo2, image1, image2, image3, image4, image5, image6, image7));

		//YOUTHCOUNCILS
		YouthCouncil youthCouncil1 = new YouthCouncil("youthcouncil1", "municipality1", "youthcouncil_description", councilLogo1, false);
		YouthCouncil youthCouncil2 = new YouthCouncil("youthcouncil2", "municipality2", "youthcouncil_description", councilLogo2, true);

		youthCouncilRepository.saveAll(List.of(youthCouncil1, youthCouncil2));

		YouthCouncilSubscription youthCouncilSubscription1 = new YouthCouncilSubscription(userList.get(0), youthCouncil1, SubscriptionRole.COUNCIL_ADMIN);
		YouthCouncilSubscription youthCouncilSubscription2 = new YouthCouncilSubscription(userList.get(1), youthCouncil2, SubscriptionRole.COUNCIL_ADMIN);
		YouthCouncilSubscription youthCouncilSubscription3 = new YouthCouncilSubscription(userList.get(2), youthCouncil1, SubscriptionRole.MODERATOR);
		YouthCouncilSubscription youthCouncilSubscription4 = new YouthCouncilSubscription(userList.get(3), youthCouncil2, SubscriptionRole.MODERATOR);
		YouthCouncilSubscription youthCouncilSubscription5 = new YouthCouncilSubscription(userList.get(4), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription6 = new YouthCouncilSubscription(userList.get(5), youthCouncil1, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription7 = new YouthCouncilSubscription(userList.get(6), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription8 = new YouthCouncilSubscription(userList.get(0), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription9 = new YouthCouncilSubscription(userList.get(4), youthCouncil1, SubscriptionRole.USER);

		youthCouncilSubscriptionRepository.saveAll(List.of(youthCouncilSubscription1, youthCouncilSubscription2, youthCouncilSubscription3, youthCouncilSubscription4, youthCouncilSubscription5, youthCouncilSubscription6, youthCouncilSubscription7, youthCouncilSubscription8, youthCouncilSubscription9));

		//ANNOUNCEMENTS
		Announcement announcement1 = new Announcement("Happening AB", "annoucement description", LocalDateTime.now(), youthCouncil1, true);
		Announcement announcement2 = new Announcement("Announcement XY", "annoucement description", LocalDateTime.now(), youthCouncil1, true);
		Announcement announcement3 = new Announcement("Activity1", "annoucement description", LocalDateTime.now(), youthCouncil1, true);
		Announcement announcement4 = new Announcement("Announcement KL", "annoucement description", LocalDateTime.now(), youthCouncil2, true);
		Announcement announcement5 = new Announcement("Activity2", "annoucement description", LocalDateTime.now(), youthCouncil2, true);
		Announcement announcement6 = new Announcement("Happening UT", "annoucement description", LocalDateTime.now(), youthCouncil2, true);

		announcementRepository.saveAll(List.of(announcement1, announcement2, announcement3, announcement4, announcement5, announcement6));

		//CALL FOR IDEAS and IDEAS

		CallForIdea callForIdea1 = new CallForIdea("call for very good ideas!", youthCouncil1, theme1, true, false);
		CallForIdea callForIdea2 = new CallForIdea("call for very very bad ideas!", youthCouncil2, theme2, true, false);

		callForIdeaRepository.saveAll(List.of(callForIdea1, callForIdea2));

		//IDEAS
		Idea idea11 = new Idea("very brilliant idea 1", subTheme1a, image3, userList.get(4), callForIdea1);
		Idea idea12 = new Idea("very not brilliant idea 2", subTheme2a, image2, userList.get(5), callForIdea1);
		Idea idea21 = new Idea("very brilliant idea 3", subTheme2c, image1, userList.get(6), callForIdea2);
		Idea idea22 = new Idea("very very brilliant idea 4", subTheme1c, image5, userList.get(5), callForIdea2);

		ideaRepository.saveAll(List.of(idea11, idea12, idea21, idea22));

		//INFORMATIVE PAGE
		InformativePage informativePage1 = new InformativePage("info page title 1", List.of("paragrah 1", "paragrah 2", "paragrah 3"), image7, null, youthCouncil1, null, true);
		InformativePage informativePage2 = new InformativePage("info page title 2", List.of("paragrah 1", "paragrah 2", "paragrah 3"), image7, null, youthCouncil1, null, true);
		InformativePage informativePage3 = new InformativePage("info page title 3", List.of("paragrah 1", "paragrah 2", "paragrah 3"), image7, null, youthCouncil2, null, true);
		InformativePage informativePage4 = new InformativePage("info page title 4", List.of("paragrah 1", "paragrah 2", "paragrah 3"), image7, null, youthCouncil2, null, true);

		informativePageRepository.saveAll(List.of(informativePage1, informativePage2, informativePage3, informativePage4));

		// ACTION POINTS
		ActionPoint actionPoint1 = new ActionPoint("action point title 1", "description", subTheme1a, ActionPointStatus.NEW, youthCouncil1, true);
		ActionPoint actionPoint2 = new ActionPoint("action point title 2", "description", subTheme2a, ActionPointStatus.INCLUDED, youthCouncil1, true);
		ActionPoint actionPoint3 = new ActionPoint("action point title 3", "description", subTheme2b, ActionPointStatus.NEW, youthCouncil2, true);
		ActionPoint actionPoint4 = new ActionPoint("action point title 4", "description", subTheme2c, ActionPointStatus.REALISED, youthCouncil2, true);

		actionPointRepository.saveAll(List.of(actionPoint1, actionPoint2, actionPoint3, actionPoint4));

		ActionPointSubscription actionPointSubscription1 = new ActionPointSubscription(userList.get(5), actionPoint1);
		ActionPointSubscription actionPointSubscription2 = new ActionPointSubscription(userList.get(6), actionPoint3);

		actionPointSubscriptionRepository.saveAll(List.of(actionPointSubscription1, actionPointSubscription2));
	}


}
