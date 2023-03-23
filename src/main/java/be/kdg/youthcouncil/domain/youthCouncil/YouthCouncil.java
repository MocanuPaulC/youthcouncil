package be.kdg.youthcouncil.domain.youthCouncil;

import be.kdg.youthcouncil.domain.moduleItems.Announcement;
import be.kdg.youthcouncil.domain.moduleItems.CallForIdea;
import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import be.kdg.youthcouncil.domain.user.User;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table (name = "youthcouncil")
public class YouthCouncil {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "youthcouncil_id")
	private long id;
	private String councilName;
	private String municipality;
	private String description;
	private String councilLogo;
	private boolean isAfterElection;

	@ManyToMany
	@ToString.Exclude
	private List<User> councilAdmins;
	@ManyToMany (targetEntity = User.class)
	@ToString.Exclude
	private List<User> councilMembers;
	@ManyToMany
	@ToString.Exclude
	private List<User> blockedMembers;
	@ManyToMany (fetch = FetchType.EAGER)
	@ToString.Exclude
	private List<ModuleItem> moduleItems;

	@OneToMany
	@ToString.Exclude
	private List<CallForIdea> callForIdeas;

	@OneToMany
	@ToString.Exclude
	private List<InformativePage> informativePages;

	@OneToMany
	@ToString.Exclude
	private List<Announcement> announcements;

	public void addCouncilAdmin(User user) {
		logger.debug("Adding council admin " + user.getFirstName() + " " + user.getLastName() + " to council " + councilName);
		councilAdmins.add(user);
	}

	public void removeCouncilAdmin(User user) {
		logger.debug("Removing council admin " + user.getFirstName() + " " + user.getLastName() + " from council " + councilName);
		councilAdmins.remove(user);
	}

	public void addCouncilMember(User user) {
		logger.debug("Adding council member " + user.getFirstName() + " " + user.getLastName() + " to council " + councilName);
		councilMembers.add(user);
	}

	public void removeCouncilMember(User user) {
		logger.debug("Removing council member " + user.getFirstName() + " " + user.getLastName() + " from council " + councilName);
		councilMembers.remove(user);
	}

	public void addBlockedMember(User user) {
		logger.debug("Adding blocked member " + user.getFirstName() + " " + user.getLastName() + " to council " + councilName);
		blockedMembers.add(user);
	}

	public void removeBlockedMember(User user) {
		logger.debug("Removing blocked member " + user.getFirstName() + " " + user.getLastName() + " from council " + councilName);
		blockedMembers.remove(user);
	}

	public void addModuleItem(ModuleItem moduleItem) {
		logger.debug("Adding module item " + moduleItem.getTitle() + " to council " + councilName);
		moduleItems.add(moduleItem);
	}

	public void removeModuleItem(ModuleItem moduleItem) {
		logger.debug("Removing module item " + moduleItem.getTitle() + " from council " + councilName);
		moduleItems.remove(moduleItem);
	}

	public void addInformativePage(InformativePage informativePage) {
		logger.debug("Adding informative page " + informativePage.getTitle() + " to council " + councilName);
		informativePages.add(informativePage);
	}

	public void removeInformativePage(InformativePage informativePage) {
		logger.debug("Removing informative page " + informativePage.getTitle() + " from council " + councilName);
		informativePages.remove(informativePage);
	}

	public void addAnnouncement(Announcement announcement) {
		logger.debug("Adding announcement " + announcement.getTitle() + " to council " + councilName);
		announcements.add(announcement);
	}

	public void removeAnnouncement(Announcement announcement) {
		logger.debug("Removing announcement " + announcement.getTitle() + " from council " + councilName);
		announcements.remove(announcement);
	}
}
