package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.domain.ModuleItem;
import be.kdg.youthcouncil.utility.Label;
import be.kdg.youthcouncil.utility.Publisher;
import be.kdg.youthcouncil.utility.SubTheme;
import be.kdg.youthcouncil.utility.Subscriber;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DiscriminatorValue("actionpoint")
@Entity
public class ActionPoint extends ModuleItem implements Publisher {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OneToOne
    private SubTheme subTheme;
    @Enumerated(EnumType.STRING)
    private Label label;

    @OneToMany
    private List<Idea> ideaList;

    public void addIdea(Idea idea) {
        logger.debug("Adding idea " + idea.getId() + idea.getTitle() + " to actionpoint" + this.getId());
        ideaList.add(idea);
    }

    public void removeIdea(Idea idea) {
        logger.debug("Removing idea " + idea.getId() + idea.getTitle() + "  from actionpoint" + this.getId());
        ideaList.remove(idea);
    }

    @Override
    public void notifySubscribers() {
        logger.debug("Notifying subscribers");

    }

    @Override
    public void subscribe(Subscriber subscriber) {
        logger.debug("Subscribing " + subscriber.toString() + " to actionpoint" + this.getId());

    }

    @Override
    public void unsubscriber(Subscriber subscriber) {
        logger.debug("Unsubscribing " + subscriber.toString() + " from actionpoint" + this.getId());

    }
}
