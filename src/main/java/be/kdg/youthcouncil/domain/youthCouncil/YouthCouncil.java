package be.kdg.youthcouncil.domain.youthCouncil;

import be.kdg.youthcouncil.domain.InformativePage;
import be.kdg.youthcouncil.domain.ModuleItem;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.moduleItems.NewsFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "youthcouncil")
public class YouthCouncil {
    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @OneToOne
    private final NewsFeed newsFeed = new NewsFeed();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "youthcouncil_id")
    private long id;
    private String councilName;
    private String municipalityName;
    private String description;
    private String councilLogo;
    private boolean isAfterElection;

    @ManyToMany
    private List<User> councilAdmins;
    @ManyToMany
    private List<User> councilMembers;
    @ManyToMany
    private List<User> blockedMembers;
    @ManyToMany
    private List<ModuleItem> defaultModuleItems;
    @OneToMany
    private List<ModuleItem> displayedModuleItems;

    @ManyToMany
    private List<InformativePage> defaultInformativePages;

    @OneToMany
    private List<InformativePage> displayedInformativePages;

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

    public void addDefaultModuleItem(ModuleItem moduleItem) {
        logger.debug("Adding default module item " + moduleItem.getTitle() + " " + moduleItem.getClass() + " to council " + councilName);
        defaultModuleItems.add(moduleItem);
    }

    public void addAllDefaultModuleItems(List<ModuleItem> moduleItems) {
        logger.debug("Adding all default module items to council " + councilName);
        moduleItems.forEach(this::addDefaultModuleItem);
    }

    public void removeDefaultModuleItem(ModuleItem moduleItem) {
        logger.debug("Removing default module item " + moduleItem.getTitle() + " " + moduleItem.getClass() + " from council " + councilName);
        defaultModuleItems.remove(moduleItem);
    }

    public void addDisplayedModuleItem(ModuleItem moduleItem) {
        logger.debug("Adding displayed module item " + moduleItem.getTitle() + " " + moduleItem.getClass() + " to council " + councilName);
        displayedModuleItems.add(moduleItem);
    }

    public void removeDisplayedModuleItem(ModuleItem moduleItem) {
        logger.debug("Removing displayed module item " + moduleItem.getTitle() + " " + moduleItem.getClass() + " from council " + councilName);
        displayedModuleItems.remove(moduleItem);
    }

    public void addDefaultInformativePage(InformativePage informativePage) {
        logger.debug("Adding default informative page " + informativePage.getTitle() + " to council " + councilName);
        defaultInformativePages.add(informativePage);
    }

    public void addAllDefaultInformativePages(List<InformativePage> informativePages) {
        logger.debug("Adding all default informative pages to council " + councilName);
        informativePages.forEach(this::addDefaultInformativePage);
    }

    public void removeDefaultInformativePage(InformativePage informativePage) {
        logger.debug("Removing default informative page " + informativePage.getTitle() + " from council " + councilName);
        defaultInformativePages.remove(informativePage);
    }

    public void addDisplayedInformativePage(InformativePage informativePage) {
        logger.debug("Adding displayed informative page " + informativePage.getTitle() + " to council " + councilName);
        displayedInformativePages.add(informativePage);
    }

    public void removeDisplayedInformativePage(InformativePage informativePage) {
        logger.debug("Removing displayed informative page " + informativePage.getTitle() + " from council " + councilName);
        displayedInformativePages.remove(informativePage);
    }

}
