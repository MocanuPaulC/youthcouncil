package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.media.Video;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "informative_pages")
public class InformativePage implements Activatable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long informativePageId;
	private String title;

	@ElementCollection
	private List<String> paragraphs;

	@OneToOne
	private Image image;
	@OneToOne
	private Video video;

	@ManyToOne
	private YouthCouncil owningYouthCouncil;

	@ManyToOne
	private InformativePage defaultInformativePage;

	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	private boolean isActive;

	public InformativePage(String title, List<String> paragraphs, Image image, Video video, YouthCouncil owningYouthCouncil, InformativePage defaultInformativePage, boolean isActive) {
		this.title = title;
		this.paragraphs = paragraphs;
		this.image = image;
		this.video = video;
		this.owningYouthCouncil = owningYouthCouncil;
		this.defaultInformativePage = defaultInformativePage;
		this.isActive = isActive;
	}

	@Override
	public boolean getActiveStatus() {
		return isActive;
	}

	@Override
	public void setActiveStatus(boolean isActive) {
		this.isActive = isActive;
	}
}
