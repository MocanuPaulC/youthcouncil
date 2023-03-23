package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.domain.interactions.Share;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class CallForIdea {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "callforidea_id")
	private long id;

	@OneToMany
	@ToString.Exclude
	private List<Idea> ideas;
	@Column
	private String title;
	@Column
	@ColumnDefault ("CURRENT_DATE")
	private LocalDate startDate;
	@Column
	@ColumnDefault ("false")
	private boolean isClosed;
	@Column
	private String image;
	@OneToMany
	@ToString.Exclude
	private List<Share> shares;

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
