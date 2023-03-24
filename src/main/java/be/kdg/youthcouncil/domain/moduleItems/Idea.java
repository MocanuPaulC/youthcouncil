package be.kdg.youthcouncil.domain.moduleItems;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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
	@Column (columnDefinition = "boolean default false")
	private boolean toModerate = true;

	private String image;

	public Idea(String title) {
		logger.debug("Creating idea " + title + " with no theme ");
		// will change this later
		this.setDescription(title);
		this.setTitle(title);
		this.toModerate = true;
	}

	@Override
	public String toString() {
		return "Idea{} " + super.toString();
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
