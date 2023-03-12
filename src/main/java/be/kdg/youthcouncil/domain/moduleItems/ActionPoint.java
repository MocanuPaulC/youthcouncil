package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.utility.Publisher;
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
@DiscriminatorValue ("actionpoint")
@Entity
public class ActionPoint extends ModuleItem implements Publisher {

	@Transient
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@OneToOne
	private SubTheme subTheme;
	@Enumerated (EnumType.STRING)
	private Label label;

	@OneToMany
	@ToString.Exclude
	private List<Idea> ideaList;

	@ElementCollection
	private List<String> images;
	@ElementCollection
	private List<String> videos;


	public void addImage(String image) {
		logger.debug("Adding image " + image + " to action point" + super.getId());
		images.add(image);
	}

	public void removeImage(String image) {
		logger.debug("Removing image " + image + " from action point" + super.getId());
		images.remove(image);
	}

	public void addVideo(String video) {
		logger.debug("Adding video " + video + " to action point" + super.getId());
		videos.add(video);
	}

	public void removeVideo(String video) {
		logger.debug("Removing video " + video + " from action point" + super.getId());
		videos.remove(video);
	}

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
