package be.kdg.youthcouncil.domain;

import be.kdg.youthcouncil.domain.interactions.Reaction;
import be.kdg.youthcouncil.domain.interactions.Share;
import be.kdg.youthcouncil.domain.moduleItems.Idea;
import be.kdg.youthcouncil.utility.Role;
import be.kdg.youthcouncil.utility.Subscriber;
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
@Table(name = "application_users")
public class User implements Subscriber {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String postcode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    private List<Idea> ideas;

    @OneToMany
    private List<Reaction> reactions;
    @OneToMany
    private List<Share> shares;


    public void addIdea(Idea idea) {
        logger.debug("Adding idea " + idea.getDescription() + " to user" + this.getId() + this.getFirstName());
        ideas.add(idea);
    }

    public void removeIdea(Idea idea) {
        logger.debug("Removing idea " + idea.getDescription() + " from user" + this.getId() + this.getFirstName());
        ideas.remove(idea);
    }

    public void addReaction(Reaction reaction) {
        logger.debug("Adding reaction " + reaction.getEmoji() + " to user" + this.getId() + this.getFirstName());
        reactions.add(reaction);
    }

    public void removeReaction(Reaction reaction) {

        logger.debug("Removing reaction " + reaction.getEmoji() + " from user" + this.getId() + this.getFirstName());
        reactions.remove(reaction);
    }

    public void addShare(Share share) {
        logger.debug("Adding share to user" + this.getId() + this.getFirstName());
        shares.add(share);
    }

    public void removeShare(Share share) {
        logger.debug("Removing share from user" + this.getId() + this.getFirstName());
        shares.remove(share);
    }

    @Override
    public void update() {

    }
}
