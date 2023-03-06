package be.kdg.youthcouncil.domain;

import be.kdg.youthcouncil.domain.interactions.Reaction;
import be.kdg.youthcouncil.domain.interactions.Share;
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
@ToString
@NoArgsConstructor
@Entity
@Table(name = "moduleItems")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("moduleItem")
public abstract class ModuleItem {


    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moduleItem_id")
    private long id;
    private String title;
    private String description;
    @ElementCollection
    private List<String> images;
    @ElementCollection
    private List<String> videos;
    private boolean isEnabled;
    @JoinColumn(name = "defaultModuleItem")
    @OneToOne
    private ModuleItem defaultModuleItem;

    @OneToMany
    private List<Reaction> reactions;

    @OneToMany
    private List<Share> shares;

    public void addImage(String image) {
        logger.debug("Adding image " + image + " to moduleItem " + title);
        images.add(image);
    }

    public void removeImage(String image) {
        logger.debug("Removing image " + image + " from moduleItem " + title);
        images.remove(image);
    }

    public void addVideo(String video) {
        logger.debug("Adding video " + video + " to moduleItem " + title);
        videos.add(video);
    }

    public void removeVideo(String video) {
        logger.debug("Removing video " + video + " from moduleItem " + title);
        videos.remove(video);
    }

    public void addReaction(Reaction reaction) {
        logger.debug("Adding reaction " + reaction.getEmoji() + " to moduleItem " + title);
        reactions.add(reaction);
    }

    public void removeReaction(Reaction reaction) {
        logger.debug("Removing reaction " + reaction.getEmoji() + " from moduleItem " + title);
        reactions.remove(reaction);
    }

    public void addShare(Share share) {
        logger.debug("Adding share to moduleItem " + title);
        shares.add(share);
    }

    public void removeShare(Share share) {
        logger.debug("Removing share from moduleItem " + title);
        shares.remove(share);
    }


}
