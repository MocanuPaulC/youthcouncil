package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.domain.ModuleItem;
import be.kdg.youthcouncil.utility.SubTheme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Idea extends ModuleItem {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ManyToOne
    private SubTheme subTheme;

    private String image;

    public void setImage(String image) {
        logger.debug("Setting image " + image + " to idea " + super.getId());
        this.image = image;
    }

    public void removeImage() {
        logger.debug("Removing image from idea " + super.getId());
        this.image = null;
    }


    public Idea(String description, SubTheme superTheme) {
        logger.debug("Creating idea " + description + " with theme " + superTheme.getSubTheme());
        this.setDescription(description);
        this.subTheme = superTheme;
    }
}
