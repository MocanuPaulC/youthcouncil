package be.kdg.youthcouncil.domain.moduleItems;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue ("call-for-idea")
public class CallForIdea extends ModuleItem {
	@Transient
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@OneToMany
	@ToString.Exclude
	private List<Idea> ideas;

	private String image;

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
