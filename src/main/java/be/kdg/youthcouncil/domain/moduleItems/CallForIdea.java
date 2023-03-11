package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.domain.ModuleItem;
import be.kdg.youthcouncil.utility.SubTheme;
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
@DiscriminatorValue("callforidea")
public class CallForIdea extends ModuleItem {
    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OneToMany
    private List<Idea> ideas;

    @ElementCollection
    private List<String> images;


    public void addImage(String image) {
        logger.debug("Adding image " + image + " to call for idea " + super.getId());
        images.add(image);
    }

    public void removeImage(String image) {
        logger.debug("Removing image " + image + " from call for idea " + super.getId());
        images.remove(image);
    }


    public Idea createIdea(String idea, SubTheme subTheme) {
        logger.debug("Creating idea " + idea + " with theme " + subTheme.getSubTheme());
        Idea newIdea = new Idea(idea, subTheme);
        ideas.add(newIdea);
        return newIdea;
    }

    public void addIdea(Idea idea) {
        logger.debug("Adding idea " + idea.getDescription() + " to callforidea" + this.getId() + this.getTitle());
        logger.debug("WIP");
    }

    public void removeIdea(Idea idea) {
        logger.debug("Removing idea " + idea.getDescription() + " from callforidea" + this.getId() + this.getTitle());
        ideas.remove(idea);
    }

}
