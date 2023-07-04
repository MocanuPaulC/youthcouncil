package be.kdg.youthcouncil.config;

import be.kdg.youthcouncil.domain.Municipality;
import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.*;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.ActionPointStatus;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.persistence.media.MediaRepository;
import be.kdg.youthcouncil.persistence.users.AdminRepository;
import be.kdg.youthcouncil.persistence.users.UserRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.MunicipalityRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.YouthCouncilRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.*;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.SubThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.modules.themes.ThemeRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.ActionPointSubscriptionRepository;
import be.kdg.youthcouncil.persistence.youthcouncil.subscriptions.YouthCouncilSubscriptionRepository;
import be.kdg.youthcouncil.utility.Notification;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
//@Profile ({"dev", "postgres"})
@AllArgsConstructor
public class DatabaseSeeder {

	private final Faker faker = new Faker();
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
	InformativePageBlockRepository informativePageBlockRepository;
	ActionPointRepository actionPointRepository;
	YouthCouncilSubscriptionRepository youthCouncilSubscriptionRepository;
	ActionPointSubscriptionRepository actionPointSubscriptionRepository;
	NotificationRepository notificationRepository;
	MunicipalityRepository municipalityJpaRepository;

	Environment env;

	@PostConstruct
	public void loadData() {
		final boolean LONG_FORMAT = Boolean.parseBoolean(
				env.getProperty("config.seeding.additional.active") != null
						? env.getProperty("config.seeding.additional.active")
						: "false"
		);
		final int NUM_USERS = Integer.parseInt(
				env.getProperty("config.seeding.additional.users") != null
						? env.getProperty("config.seeding.additional.users")
						: "300"
		);
		final int MAX_ANN = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.annoucements") != null
						? env.getProperty("config.seeding.additional.max.annoucements")
						: "30"
		);
		final int MAX_AC = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.actionpoints") != null
						? env.getProperty("config.seeding.additional.max.actionpoints")
						: "5"
		);
		final int MAX_CFI = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.callforideas.cfi") != null
						? env.getProperty("config.seeding.additional.max.callforideas.cfi")
						: "3"
		);
		final int MAX_IDEAS = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.callforideas.ideas") != null
						? env.getProperty("config.seeding.additional.max.callforideas.ideas")
						: "50"
		);
		final int MAX_INFO_PAGES = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.infopages.ip") != null
						? env.getProperty("config.seeding.additional.max.infopages.ip")
						: "5"
		);
		final int MAX_INFO_PAGE_BLOCKS = Integer.parseInt(
				env.getProperty("config.seeding.additional.max.infopages.infopageblocks") != null
						? env.getProperty("config.seeding.additional.max.infopages.infopageblocks")
						: "15"
		);


		final List<Municipality> municipalities = municipalityJpaRepository.saveAll(List.of(
				new Municipality(21001L, "Anderlecht"),
				new Municipality(21002L, "Oudergem"),
				new Municipality(21003L, "Sint-Agatha-Berchem"),
				new Municipality(21004L, "Brussel"),
				new Municipality(21005L, "Etterbeek"),
				new Municipality(21006L, "Evere"),
				new Municipality(21007L, "Vorst"),
				new Municipality(21008L, "Ganshoren"),
				new Municipality(21009L, "Elsene"),
				new Municipality(21010L, "Jette"),
				new Municipality(21011L, "Koekelberg"),
				new Municipality(21012L, "Sint-Jans-Molenbeek"),
				new Municipality(21013L, "Sint-Gillis"),
				new Municipality(21014L, "Sint-Joost-ten-Node"),
				new Municipality(21015L, "Schaarbeek"),
				new Municipality(21016L, "Ukkel"),
				new Municipality(21017L, "Watermaal-Bosvoorde"),
				new Municipality(21018L, "Sint-Lambrechts-Woluwe"),
				new Municipality(21019L, "Sint-Pieters-Woluwe"),
				new Municipality(11001L, "Aartselaar"),
				new Municipality(110023L, "Berendrecht-Zandvliet-Lillo"),
				new Municipality(110028L, "Merksem"),
				new Municipality(110025L, "Deurne"),
				new Municipality(110027L, "Hoboken"),
				new Municipality(110021L, "Antwerpen"),
				new Municipality(110022L, "Berchem"),
				new Municipality(110024L, "Borgerhout"),
				new Municipality(110029L, "Wilrijk"),
				new Municipality(110026L, "Ekeren"),
				new Municipality(11004L, "Boechout"),
				new Municipality(11005L, "Boom"),
				new Municipality(11007L, "Borsbeek"),
				new Municipality(11008L, "Brasschaat"),
				new Municipality(11009L, "Brecht"),
				new Municipality(11013L, "Edegem"),
				new Municipality(11016L, "Essen"),
				new Municipality(11018L, "Hemiksem"),
				new Municipality(11021L, "Hove"),
				new Municipality(11022L, "Kalmthout"),
				new Municipality(11023L, "Kapellen"),
				new Municipality(11024L, "Kontich"),
				new Municipality(11025L, "Lint"),
				new Municipality(11057L, "Malle"),
				new Municipality(11029L, "Mortsel"),
				new Municipality(11030L, "Niel"),
				new Municipality(11035L, "Ranst"),
				new Municipality(11037L, "Rumst"),
				new Municipality(11038L, "Schelle"),
				new Municipality(11039L, "Schilde"),
				new Municipality(11040L, "Schoten"),
				new Municipality(11044L, "Stabroek"),
				new Municipality(11050L, "Wijnegem"),
				new Municipality(11052L, "Wommelgem"),
				new Municipality(11053L, "Wuustwezel"),
				new Municipality(11054L, "Zandhoven"),
				new Municipality(11055L, "Zoersel"),
				new Municipality(11056L, "Zwijndrecht"),
				new Municipality(12002L, "Berlaar"),
				new Municipality(12005L, "Bonheiden"),
				new Municipality(12007L, "Bornem"),
				new Municipality(12009L, "Duffel"),
				new Municipality(12014L, "Heist-op-den-Berg"),
				new Municipality(12021L, "Lier"),
				new Municipality(12025L, "Mechelen"),
				new Municipality(12026L, "Nijlen"),
				new Municipality(12029L, "Putte"),
				new Municipality(12041L, "Puurs-Sint-Amands"),
				new Municipality(12035L, "Sint-Katelijne-Waver"),
				new Municipality(12040L, "Willebroek"),
				new Municipality(13001L, "Arendonk"),
				new Municipality(13002L, "Baarle-Hertog"),
				new Municipality(13003L, "Balen"),
				new Municipality(13004L, "Beerse"),
				new Municipality(13006L, "Dessel"),
				new Municipality(13008L, "Geel"),
				new Municipality(13010L, "Grobbendonk"),
				new Municipality(13011L, "Herentals"),
				new Municipality(13012L, "Herenthout"),
				new Municipality(13013L, "Herselt"),
				new Municipality(13014L, "Hoogstraten"),
				new Municipality(13016L, "Hulshout"),
				new Municipality(13017L, "Kasterlee"),
				new Municipality(13053L, "Laakdal"),
				new Municipality(13019L, "Lille"),
				new Municipality(13021L, "Meerhout"),
				new Municipality(13023L, "Merksplas"),
				new Municipality(13025L, "Mol"),
				new Municipality(13029L, "Olen"),
				new Municipality(13031L, "Oud-Turnhout"),
				new Municipality(13035L, "Ravels"),
				new Municipality(13036L, "Retie"),
				new Municipality(13037L, "Rijkevorsel"),
				new Municipality(13040L, "Turnhout"),
				new Municipality(13044L, "Vorselaar"),
				new Municipality(13046L, "Vosselaar"),
				new Municipality(13049L, "Westerlo"),
				new Municipality(23105L, "Affligem"),
				new Municipality(23002L, "Asse"),
				new Municipality(23003L, "Beersel"),
				new Municipality(23009L, "Bever"),
				new Municipality(23016L, "Dilbeek"),
				new Municipality(23098L, "Drogenbos"),
				new Municipality(23023L, "Galmaarden"),
				new Municipality(23024L, "Gooik"),
				new Municipality(23025L, "Grimbergen"),
				new Municipality(23027L, "Halle"),
				new Municipality(23032L, "Herne"),
				new Municipality(23033L, "Hoeilaart"),
				new Municipality(23038L, "Kampenhout"),
				new Municipality(23039L, "Kapelle-op-den-Bos"),
				new Municipality(23099L, "Kraainem"),
				new Municipality(23104L, "Lennik"),
				new Municipality(23044L, "Liedekerke"),
				new Municipality(23100L, "Linkebeek"),
				new Municipality(23045L, "Londerzeel"),
				new Municipality(23047L, "Machelen"),
				new Municipality(23050L, "Meise"),
				new Municipality(23052L, "Merchtem"),
				new Municipality(23060L, "Opwijk"),
				new Municipality(23062L, "Overijse"),
				new Municipality(23064L, "Pepingen"),
				new Municipality(23101L, "Sint-Genesius-Rode"),
				new Municipality(23097L, "Roosdaal"),
				new Municipality(23077L, "Sint-Pieters-Leeuw"),
				new Municipality(23081L, "Steenokkerzeel"),
				new Municipality(23086L, "Ternat"),
				new Municipality(23088L, "Vilvoorde"),
				new Municipality(23102L, "Wemmel"),
				new Municipality(23103L, "Wezembeek-Oppem"),
				new Municipality(23094L, "Zaventem"),
				new Municipality(23096L, "Zemst"),
				new Municipality(24001L, "Aarschot"),
				new Municipality(24007L, "Begijnendijk"),
				new Municipality(24008L, "Bekkevoort"),
				new Municipality(24009L, "Bertem"),
				new Municipality(24011L, "Bierbeek"),
				new Municipality(24014L, "Boortmeerbeek"),
				new Municipality(24016L, "Boutersem"),
				new Municipality(24020L, "Diest"),
				new Municipality(24028L, "Geetbets"),
				new Municipality(24137L, "Glabbeek"),
				new Municipality(24033L, "Haacht"),
				new Municipality(24038L, "Herent"),
				new Municipality(24041L, "Hoegaarden"),
				new Municipality(24043L, "Holsbeek"),
				new Municipality(24045L, "Huldenberg"),
				new Municipality(24048L, "Keerbergen"),
				new Municipality(24054L, "Kortenaken"),
				new Municipality(24055L, "Kortenberg"),
				new Municipality(24059L, "Landen"),
				new Municipality(24130L, "Zoutleeuw"),
				new Municipality(24133L, "Linter"),
				new Municipality(24062L, "Leuven"),
				new Municipality(24066L, "Lubbeek"),
				new Municipality(24134L, "Scherpenheuvel-Zichem"),
				new Municipality(24086L, "Oud-Heverlee"),
				new Municipality(24094L, "Rotselaar"),
				new Municipality(24104L, "Tervuren"),
				new Municipality(24135L, "Tielt-Winge"),
				new Municipality(24107L, "Tienen"),
				new Municipality(24109L, "Tremelo"),
				new Municipality(31003L, "Beernem"),
				new Municipality(31004L, "Blankenberge"),
				new Municipality(31005L, "Brugge"),
				new Municipality(31006L, "Damme"),
				new Municipality(31012L, "Jabbeke"),
				new Municipality(31043L, "Knokke-Heist"),
				new Municipality(31022L, "Oostkamp"),
				new Municipality(31033L, "Torhout"),
				new Municipality(31040L, "Zedelgem"),
				new Municipality(31042L, "Zuienkerke"),
				new Municipality(34002L, "Anzegem"),
				new Municipality(34003L, "Avelgem"),
				new Municipality(34022L, "Kortrijk"),
				new Municipality(34009L, "Deerlijk"),
				new Municipality(34043L, "Spiere-Helkijn"),
				new Municipality(34013L, "Harelbeke"),
				new Municipality(34023L, "Kuurne"),
				new Municipality(34025L, "Lendelede"),
				new Municipality(34027L, "Menen"),
				new Municipality(34040L, "Waregem"),
				new Municipality(34041L, "Wevelgem"),
				new Municipality(34042L, "Zwevegem"),
				new Municipality(32003L, "Diksmuide"),
				new Municipality(32006L, "Houthulst"),
				new Municipality(32010L, "Koekelare"),
				new Municipality(32011L, "Kortemark"),
				new Municipality(32030L, "Lo-Reninge"),
				new Municipality(38002L, "Alveringem"),
				new Municipality(38025L, "Veurne"),
				new Municipality(38014L, "Koksijde"),
				new Municipality(38008L, "De Panne"),
				new Municipality(38016L, "Nieuwpoort"),
				new Municipality(35002L, "Bredene"),
				new Municipality(35029L, "De Haan"),
				new Municipality(35005L, "Gistel"),
				new Municipality(35006L, "Ichtegem"),
				new Municipality(35011L, "Middelkerke"),
				new Municipality(35013L, "Oostende"),
				new Municipality(35014L, "Oudenburg"),
				new Municipality(36006L, "Hooglede"),
				new Municipality(36007L, "Ingelmunster"),
				new Municipality(36008L, "Izegem"),
				new Municipality(36010L, "Ledegem"),
				new Municipality(36011L, "Lichtervelde"),
				new Municipality(36012L, "Moorslede"),
				new Municipality(36015L, "Roeselare"),
				new Municipality(36019L, "Staden"),
				new Municipality(37020L, "Ardooie"),
				new Municipality(37002L, "Dentergem"),
				new Municipality(37007L, "Meulebeke"),
				new Municipality(37010L, "Oostrozebeke"),
				new Municipality(37011L, "Pittem"),
				new Municipality(37012L, "Ruiselede"),
				new Municipality(37015L, "Tielt"),
				new Municipality(37017L, "Wielsbeke"),
				new Municipality(37018L, "Wingene"),
				new Municipality(33039L, "Heuvelland"),
				new Municipality(33040L, "Langemark-Poelkapelle"),
				new Municipality(33016L, "Mesen"),
				new Municipality(33021L, "Poperinge"),
				new Municipality(33041L, "Vleteren"),
				new Municipality(33029L, "Wervik"),
				new Municipality(33011L, "Ieper"),
				new Municipality(33037L, "Zonnebeke"),
				new Municipality(41002L, "Aalst"),
				new Municipality(41011L, "Denderleeuw"),
				new Municipality(41082L, "Erpe-Mere"),
				new Municipality(41018L, "Geraardsbergen"),
				new Municipality(41024L, "Haaltert"),
				new Municipality(41027L, "Herzele"),
				new Municipality(41034L, "Lede"),
				new Municipality(41048L, "Ninove"),
				new Municipality(41063L, "Sint-Lievens-Houtem"),
				new Municipality(41081L, "Zottegem"),
				new Municipality(45035L, "Oudenaarde"),
				new Municipality(45059L, "Brakel"),
				new Municipality(45062L, "Horebeke"),
				new Municipality(45060L, "Kluisbergen"),
				new Municipality(45068L, "Kruisem"),
				new Municipality(45063L, "Lierde"),
				new Municipality(45064L, "Maarkedal"),
				new Municipality(45041L, "Ronse"),
				new Municipality(45061L, "Wortegem-Petegem"),
				new Municipality(45065L, "Zwalm"),
				new Municipality(43002L, "Assenede"),
				new Municipality(43005L, "Eeklo"),
				new Municipality(43007L, "Kaprijke"),
				new Municipality(43010L, "Maldegem"),
				new Municipality(43014L, "Sint-Laureins"),
				new Municipality(43018L, "Zelzate"),
				new Municipality(44084L, "Aalter"),
				new Municipality(44012L, "De Pinte"),
				new Municipality(44083L, "Deinze"),
				new Municipality(44013L, "Destelbergen"),
				new Municipality(44019L, "Evergem"),
				new Municipality(44021L, "Gent"),
				new Municipality(44020L, "Gavere"),
				new Municipality(44085L, "Lievegem"),
				new Municipality(44034L, "Lochristi"),
				new Municipality(44040L, "Melle"),
				new Municipality(44043L, "Merelbeke"),
				new Municipality(44045L, "Moerbeke"),
				new Municipality(44048L, "Nazareth"),
				new Municipality(44052L, "Oosterzele"),
				new Municipality(44064L, "Sint-Martens-Latem"),
				new Municipality(44073L, "Wachtebeke"),
				new Municipality(44081L, "Zulte"),
				new Municipality(46003L, "Beveren"),
				new Municipality(46013L, "Kruibeke"),
				new Municipality(46014L, "Lokeren"),
				new Municipality(46021L, "Sint-Niklaas"),
				new Municipality(46020L, "Sint-Gillis-Waas"),
				new Municipality(46024L, "Stekene"),
				new Municipality(46025L, "Temse"),
				new Municipality(42003L, "Berlare"),
				new Municipality(42004L, "Buggenhout"),
				new Municipality(42008L, "Hamme"),
				new Municipality(42010L, "Laarne"),
				new Municipality(42011L, "Lebbeke"),
				new Municipality(42006L, "Dendermonde"),
				new Municipality(42023L, "Waasmunster"),
				new Municipality(42025L, "Wetteren"),
				new Municipality(42026L, "Wichelen"),
				new Municipality(42028L, "Zele"),
				new Municipality(71002L, "As"),
				new Municipality(71004L, "Beringen"),
				new Municipality(71034L, "Leopoldsburg"),
				new Municipality(71011L, "Diepenbeek"),
				new Municipality(71016L, "Genk"),
				new Municipality(71017L, "Gingelom"),
				new Municipality(71020L, "Halen"),
				new Municipality(71069L, "Ham"),
				new Municipality(71022L, "Hasselt"),
				new Municipality(71024L, "Herk-de-Stad"),
				new Municipality(71070L, "Heusden-Zolder"),
				new Municipality(71037L, "Lummen"),
				new Municipality(71045L, "Nieuwerkerken"),
				new Municipality(71053L, "Sint-Truiden"),
				new Municipality(71057L, "Tessenderlo"),
				new Municipality(71066L, "Zonhoven"),
				new Municipality(71067L, "Zutendaal"),
				new Municipality(72003L, "Bocholt"),
				new Municipality(72004L, "Bree"),
				new Municipality(72041L, "Dilsen-Stokkem"),
				new Municipality(72037L, "Hamont-Achel"),
				new Municipality(72038L, "Hechtel-Eksel"),
				new Municipality(72039L, "Houthalen-Helchteren"),
				new Municipality(72018L, "Kinrooi"),
				new Municipality(72020L, "Lommel"),
				new Municipality(72021L, "Maaseik"),
				new Municipality(72042L, "Oudsbergen"),
				new Municipality(72030L, "Peer"),
				new Municipality(72043L, "Pelt"),
				new Municipality(73001L, "Alken"),
				new Municipality(73006L, "Bilzen"),
				new Municipality(73109L, "Voeren"),
				new Municipality(73022L, "Heers"),
				new Municipality(73028L, "Herstappe"),
				new Municipality(73032L, "Hoeselt"),
				new Municipality(73040L, "Kortessem"),
				new Municipality(73042L, "Lanaken"),
				new Municipality(73009L, "Borgloon"),
				new Municipality(73107L, "Maasmechelen"),
				new Municipality(73066L, "Riemst"),
				new Municipality(73083L, "Tongeren"),
				new Municipality(73098L, "Wellen")
		));

		// USERS
		//		adminRepository.save(new GeneralAdmin("gadmin", encoder.encode("gadmin")));


		List<String> userNames = List.of("cadmin1", "cadmin2", "moderator1", "moderator2", "member1", "member2", "member3");
		List<Integer> userPostcodes = List.of(23423, 457456, 8325, 57809, 12362, 6799, 21356);
		List<PlatformUser> tmpUserList = new ArrayList<>();

		tmpUserList.addAll(IntStream.range(0, 7).mapToObj(i -> {
			String n = userNames.get(i);
			return new PlatformUser(n, n, n + "@localhost", userPostcodes.get(0)
			                                                             .toString(), n, encoder.encode(n));
		}).toList());


		PlatformUser notifiedUser = new PlatformUser("notified", "notified", "notified@localhost", "23423", "notified", encoder.encode("notified"));
		Notification seenNotification = new Notification("seen notification");
		notificationRepository.save(seenNotification);
		seenNotification.setRead(true);
		Notification unseenNotification = new Notification("unseen notification");
		notificationRepository.save(unseenNotification);
		notifiedUser.addNotification(seenNotification);
		notifiedUser.addNotification(unseenNotification);
		tmpUserList.add(notifiedUser);


		PlatformUser ga = new PlatformUser("gadmin", "gadmin", "gadmin@localhost", "23423", "gadmin", encoder.encode("gadmin"));
		ga.setGA(true);
		tmpUserList.add(ga);

		if (LONG_FORMAT) {
			tmpUserList.addAll(IntStream.range(0, NUM_USERS).mapToObj(n -> new PlatformUser(
					faker.name().firstName(),
					faker.name().lastName(),
					faker.internet().emailAddress(),
					municipalities.get(faker.number().numberBetween(0, municipalities.size())).getId().toString(),
					faker.name().username(),
					faker.internet().password()
			)).toList());
		}

		final List<PlatformUser> userList = userRepository.saveAll(tmpUserList);


		//THEMES

		Theme theme1 = new Theme("a theme");
		Theme theme2 = new Theme("b theme");
		Theme theme3 = new Theme("c theme");

		final List<Theme> themes = themeRepository.saveAll(List.of(theme1, theme2, theme3));

		SubTheme subTheme1a = new SubTheme("a theme subtheme 1", theme1);
		SubTheme subTheme2a = new SubTheme("a theme subtheme 2", theme1);
		SubTheme subTheme1b = new SubTheme("b theme subtheme 1", theme2);
		SubTheme subTheme2b = new SubTheme("b theme subtheme 2", theme2);
		SubTheme subTheme1c = new SubTheme("c theme subtheme 1", theme3);
		SubTheme subTheme2c = new SubTheme("c theme subtheme 2", theme3);

		final List<SubTheme> subThemes = subThemeRepository.saveAll(List.of(subTheme1b, subTheme1a, subTheme1c, subTheme2c, subTheme2b, subTheme2a));

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

		ArrayList<Long> tmpYouthCounilsId = new ArrayList<>();
		tmpYouthCounilsId.addAll(List.of(11029L, 24054L));

		//YOUTHCOUNCILS
		YouthCouncil youthCouncil1 = new YouthCouncil("youthcouncil1", municipalityJpaRepository.findById(tmpYouthCounilsId.get(0))
		                                                                                        .orElseThrow(), "youthcouncil_description", councilLogo1, false);
		YouthCouncil youthCouncil2 = new YouthCouncil("youthcouncil2", municipalityJpaRepository.findById(tmpYouthCounilsId.get(1))
		                                                                                        .orElseThrow(), "youthcouncil_description", councilLogo2, true);

		List<YouthCouncil> tmpYouthCouncils = new ArrayList<>();
		tmpYouthCouncils.addAll(List.of(youthCouncil1, youthCouncil2));

		if (LONG_FORMAT) {
			tmpYouthCouncils.addAll(userList.subList(9, userList.size())
			                                .stream()
			                                .map(user -> Long.parseLong(user.getPostcode()))
			                                .distinct()
			                                .map(m -> {
				                                if (tmpYouthCounilsId.contains(m)) {
					                                return null;
				                                }
				                                return new YouthCouncil(
						                                faker.company().name(),
						                                municipalities.stream()
						                                              .filter(mO -> Objects.equals(mO.getId(), m))
						                                              .findFirst()
						                                              .get(),
						                                faker.lorem().maxLengthSentence(254),
						                                image4,
						                                faker.bool().bool()
				                                );
			                                })
			                                .filter(Objects::nonNull)
			                                .toList());
		}
		final List<YouthCouncil> youthCouncils = youthCouncilRepository.saveAll(tmpYouthCouncils);

		YouthCouncilSubscription youthCouncilSubscription1 = new YouthCouncilSubscription(userList.get(0), youthCouncil1, SubscriptionRole.COUNCIL_ADMIN);
		YouthCouncilSubscription youthCouncilSubscription3 = new YouthCouncilSubscription(userList.get(2), youthCouncil1, SubscriptionRole.MODERATOR);
		YouthCouncilSubscription youthCouncilSubscription6 = new YouthCouncilSubscription(userList.get(5), youthCouncil1, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription9 = new YouthCouncilSubscription(userList.get(4), youthCouncil1, SubscriptionRole.USER);

		YouthCouncilSubscription youthCouncilSubscription2 = new YouthCouncilSubscription(userList.get(1), youthCouncil2, SubscriptionRole.COUNCIL_ADMIN);
		YouthCouncilSubscription youthCouncilSubscription4 = new YouthCouncilSubscription(userList.get(3), youthCouncil2, SubscriptionRole.MODERATOR);
		YouthCouncilSubscription youthCouncilSubscription5 = new YouthCouncilSubscription(userList.get(4), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription7 = new YouthCouncilSubscription(userList.get(6), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription8 = new YouthCouncilSubscription(userList.get(0), youthCouncil2, SubscriptionRole.USER);
		YouthCouncilSubscription youthCouncilSubscription10 = new YouthCouncilSubscription(userList.get(userList.size() - 1), youthCouncil2, SubscriptionRole.USER);

		List<YouthCouncilSubscription> tmpSubscriptions = new ArrayList<>(List.of(youthCouncilSubscription1, youthCouncilSubscription2, youthCouncilSubscription3, youthCouncilSubscription4, youthCouncilSubscription5, youthCouncilSubscription6, youthCouncilSubscription7, youthCouncilSubscription8, youthCouncilSubscription9, youthCouncilSubscription10));

		// 9 base users
		if (LONG_FORMAT) {
			tmpSubscriptions.addAll(userList.subList(9, userList.size()).stream().map(user -> {
				List<YouthCouncil> countils = youthCouncils.stream()
				                                           .filter(yc -> yc.getMunicipality()
				                                                           .equals(municipalities.stream()
				                                                                                 .filter(m -> m.getId() == Integer.parseInt(user.getPostcode()))
				                                                                                 .findFirst()
				                                                                                 .get()
				                                                                                 .getName())).toList();
				return new YouthCouncilSubscription(
						user,
						countils.get(faker.number().numberBetween(0, countils.size())),
						faker.number().randomDouble(2, 0, 1) > 0.75 ? SubscriptionRole.MODERATOR : SubscriptionRole.USER
				);
			}).toList());
		}
		final List<YouthCouncilSubscription> subscriptions = youthCouncilSubscriptionRepository.saveAll(tmpSubscriptions);

		//ANNOUNCEMENTS
		Announcement announcement1 = new Announcement("Happening AB", "annoucement description", LocalDateTime.now(), youthCouncil1, ModuleStatus.DISPLAYED);
		Announcement announcement2 = new Announcement("Announcement XY", "annoucement description", LocalDateTime.now(), youthCouncil1, ModuleStatus.DISPLAYED);
		Announcement announcement3 = new Announcement("Activity1", "annoucement description", LocalDateTime.now(), youthCouncil1, ModuleStatus.DISPLAYED);
		Announcement announcement4 = new Announcement("Announcement KL", "annoucement description", LocalDateTime.now(), youthCouncil2, ModuleStatus.DISPLAYED);
		Announcement announcement5 = new Announcement("Activity2", "annoucement description", LocalDateTime.now(), youthCouncil2, ModuleStatus.DISPLAYED);
		Announcement announcement6 = new Announcement("Happening UT", "annoucement description", LocalDateTime.now(), youthCouncil2, ModuleStatus.DISPLAYED);

		List<Announcement> tmpAnnoucements = new ArrayList<>(List.of(announcement1, announcement2, announcement3, announcement4, announcement5, announcement6));

		//CALL FOR IDEAS and IDEAS

		CallForIdea callForIdea1 = new CallForIdea("call for very good ideas!", youthCouncil1, theme1, ModuleStatus.DISPLAYED, false);
		CallForIdea callForIdea2 = new CallForIdea("call for very very bad ideas!", youthCouncil2, theme2, ModuleStatus.DISPLAYED, false);

		List<CallForIdea> tmpCallForIdeas = new ArrayList<>(List.of(callForIdea1, callForIdea2));

		InformativePage informativePage1 = new InformativePage("info-page-title-1", false, youthCouncil1);
		InformativePage informativePage2 = new InformativePage("info-page-title-2", false, youthCouncil1);
		InformativePage informativePage3 = new InformativePage("info-page-title-3", false, youthCouncil2);
		InformativePage informativePage4 = new InformativePage("info-page-title-4", false, youthCouncil2);
		InformativePage informativePage5 = new InformativePage("default-info-page", true, null);

		List<InformativePage> tmpInformativePages = new ArrayList<>(List.of(informativePage1, informativePage2, informativePage3, informativePage4));

		// ACTION POINTS
		ActionPoint actionPoint1 = new ActionPoint("action point title 1", "description", subTheme1a, ActionPointStatus.NEW, youthCouncil1, ModuleStatus.DISPLAYED);
		ActionPoint actionPoint2 = new ActionPoint("action point title 2", "description", subTheme2a, ActionPointStatus.INCLUDED, youthCouncil1, ModuleStatus.DISPLAYED);
		ActionPoint actionPoint3 = new ActionPoint("action point title 3", "description", subTheme2b, ActionPointStatus.NEW, youthCouncil2, ModuleStatus.DISPLAYED);
		ActionPoint actionPoint4 = new ActionPoint("action point title 4", "description", subTheme2c, ActionPointStatus.REALISED, youthCouncil2, ModuleStatus.DISPLAYED);

		List<ActionPoint> tmpActionPoints = new ArrayList<>(List.of(actionPoint1, actionPoint2, actionPoint3, actionPoint4));

		if (LONG_FORMAT) {
			youthCouncils.subList(2, youthCouncils.size()).forEach(yc -> {
				tmpAnnoucements.addAll(IntStream.range(1, faker.number().numberBetween(0, MAX_ANN))
				                                .mapToObj(n -> new Announcement(
						                                faker.lorem().maxLengthSentence(50),
						                                faker.lorem().maxLengthSentence(254),
						                                faker.date().birthday().toLocalDateTime(),
						                                yc,
						                                ModuleStatus.values()[faker.number().numberBetween(0, 3)]
				                                ))
				                                .toList());
				tmpCallForIdeas.addAll(IntStream.range(1, faker.number().numberBetween(0, MAX_CFI))
				                                .mapToObj(n -> new CallForIdea(
						                                faker.lorem().maxLengthSentence(50),
						                                yc,
						                                themes.get(faker.number().numberBetween(0, themes.size())),
						                                ModuleStatus.values()[faker.number().numberBetween(0, 3)],
						                                faker.bool().bool()
				                                ))
				                                .toList());
				tmpInformativePages.addAll(IntStream.range(1, faker.number().numberBetween(0, MAX_INFO_PAGES))
				                                    .mapToObj(n -> {
					                                    InformativePage informativePage = new InformativePage(
							                                    faker.lorem().maxLengthSentence(20),
							                                    false,
							                                    yc
					                                    );
					                                    List<InformativePageBlock> blocks = IntStream.range(0, faker.number()
					                                                                                                .numberBetween(0, MAX_INFO_PAGE_BLOCKS))
					                                                                                 .mapToObj(bn -> new InformativePageBlock(
							                                                                                 bn,
							                                                                                 faker.lorem()
							                                                                                      .paragraph(faker.number()
							                                                                                                      .numberBetween(0, 10)),
							                                                                                 List.of(
									                                                                                     BlockType.HEADER_BIG,
									                                                                                     BlockType.HEADER_MEDIUM,
									                                                                                     BlockType.HEADER_SMALL,
									                                                                                     BlockType.PARAGRAPH
							                                                                                     )
							                                                                                     .get(faker.number()
							                                                                                               .numberBetween(0, 4))))
					                                                                                 .toList();
					                                    informativePage.setInfoPageBlocks(informativePageBlockRepository.saveAll(blocks));

					                                    return informativePage;
				                                    })
				                                    .toList());
				tmpActionPoints.addAll(
						IntStream.range(1, faker.number().numberBetween(0, MAX_AC))
						         .mapToObj(an -> new ActionPoint(
								         faker.lorem().maxLengthSentence(20),
								         subThemes.get(faker.number().numberBetween(0, subThemes.size())),
								         false,
								         yc
						         )).toList()
				);
			});
		}
		final List<Announcement> announcements = announcementRepository.saveAll(tmpAnnoucements);

		final List<CallForIdea> callForIdeas = callForIdeaRepository.saveAll(tmpCallForIdeas);

		final List<InformativePage> informativePages = informativePageRepository.saveAll(tmpInformativePages);

		final List<ActionPoint> actionPoints = actionPointRepository.saveAll(tmpActionPoints);

		//IDEAS
		Idea idea11 = new Idea("very brilliant idea 1", subTheme1a, image3, userList.get(4), callForIdea1);
		Idea idea12 = new Idea("very not brilliant idea 2", subTheme2a, image2, userList.get(5), callForIdea1);
		Idea idea21 = new Idea("very brilliant idea 3", subTheme2c, image1, userList.get(6), callForIdea2);
		Idea idea22 = new Idea("very very brilliant idea 4", subTheme1c, image5, userList.get(5), callForIdea2);

		List<Idea> tmpIdeas = new ArrayList<>(List.of(idea11, idea12, idea21, idea22));
		if (LONG_FORMAT) {
			callForIdeas.subList(2, callForIdeas.size()).forEach(cfi -> {
				List<SubTheme> ideaTheme = subThemes.stream()
				                                    .filter(st -> st.getSuperTheme().equals(cfi.getTheme()))
				                                    .toList();
				List<PlatformUser> ideaUser = subscriptions.stream()
				                                           .filter(s -> s.getYouthCouncil()
				                                                         .equals(cfi.getOwningYouthCouncil()))
				                                           .map(YouthCouncilSubscription::getSubscriber)
				                                           .toList();

				tmpIdeas.addAll(IntStream.range(1, faker.number().numberBetween(0, MAX_IDEAS)).mapToObj(in -> new Idea(
						faker.lorem().maxLengthSentence(254),
						ideaTheme.get(faker.number().numberBetween(0, ideaTheme.size())),
						image1,
						ideaUser.get(faker.number().numberBetween(0, ideaUser.size())),
						cfi
				)).toList());
			});
		}
		final List<Idea> ideas = ideaRepository.saveAll(tmpIdeas);

		//INFORMATIVE PAGE


		ActionPointSubscription actionPointSubscription1 = new ActionPointSubscription(userList.get(5), actionPoint1);
		ActionPointSubscription actionPointSubscription2 = new ActionPointSubscription(userList.get(6), actionPoint3);
		ActionPointSubscription actionPointSubscription3 = new ActionPointSubscription(userList.get(userList.size() - 1), actionPoint3);

		actionPointSubscriptionRepository.saveAll(List.of(actionPointSubscription1, actionPointSubscription2, actionPointSubscription3));
	}


}
