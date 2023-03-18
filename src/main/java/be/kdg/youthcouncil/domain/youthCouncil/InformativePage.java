package be.kdg.youthcouncil.domain.youthCouncil;

import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table (name = "informative_pages")
public class InformativePage {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "informative_page_id")
	private long id;
	private String title;

	@ManyToOne
	private YouthCouncil owningYouthCouncil;
	@ElementCollection ()
	@LazyCollection (LazyCollectionOption.FALSE)
	private List<String> paragraphs;
	@ElementCollection ()
	@LazyCollection (LazyCollectionOption.FALSE)
	private List<String> images;
	@ElementCollection ()
	@LazyCollection (LazyCollectionOption.FALSE)
	private List<String> videos;

	@OneToOne
	private InformativePage defaultInfoPage;

	@OneToMany
	@ToString.Exclude
	private List<ModuleItem> moduleItems;
	private boolean isEnabled;

	public void addParagraph(String paragraph) {
		logger.debug("Adding paragraph " + paragraph + " to page " + title);
		paragraphs.add(paragraph);
	}

	public void removeParagraph(String paragraph) {
		logger.debug("Removing paragraph " + paragraph + " from page " + title);
		paragraphs.remove(paragraph);
	}

	public void addImage(String image) {
		logger.debug("Adding image " + image + " to page " + title);
		images.add(image);
	}

	public void removeImage(String image) {
		logger.debug("Removing image " + image + " from page " + title);
		images.remove(image);
	}

	public void addVideo(String video) {
		logger.debug("Adding video " + video + " to page " + title);
		videos.add(video);
	}

	public void removeVideo(String video) {
		logger.debug("Removing video " + video + " from page " + title);
		videos.remove(video);
	}

	public void addModuleItem(ModuleItem moduleItem) {
		logger.debug("Adding moduleItem " + moduleItem.getTitle() + " to page " + title);
		moduleItems.add(moduleItem);
	}

	public void removeModuleItem(ModuleItem moduleItem) {
		logger.debug("Removing moduleItem " + moduleItem.getTitle() + " from page " + title);
		moduleItems.remove(moduleItem);
	}


}
