package be.kdg.youthcouncil.domain.moduleItems;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue ("idea")
public class Idea extends ModuleItem {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ManyToOne
	private SubTheme subTheme;

	private String image;

	public Idea(String description, SubTheme superTheme) {
		logger.debug("Creating idea " + description + " with theme " + superTheme.getSubTheme());
		this.setDescription(description);
		this.subTheme = superTheme;
	}

	public void setImage(String image) {
		logger.debug("Setting image " + image + " to idea " + super.getId());
		this.image = image;
	}

	public void removeImage() {
		logger.debug("Removing image from idea " + super.getId());
		this.image = null;
	}
}
