package be.kdg.youthcouncil.domain.moduleItems;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Getter
@Setter
@AllArgsConstructor
@ToString
@DiscriminatorValue ("announcement")
@Entity
public class Announcement extends ModuleItem {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public Announcement(String title, String description) {
		logger.debug("Creating announcement " + title + " with description " + description);
		this.setTitle(title);
		this.setDescription(description);
	}

}
