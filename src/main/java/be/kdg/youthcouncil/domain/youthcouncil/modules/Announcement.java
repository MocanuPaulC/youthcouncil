package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.media.Media;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement implements Activatable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long announcementId;
	private String title;
	private String description;
	private LocalDateTime announcementTime;
	@OneToMany(fetch = FetchType.LAZY)
	private List<Media> media;
	@ManyToOne
	private YouthCouncil owningYouthCouncil;


	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isActive;

	public Announcement(String title, String description, LocalDateTime announcementTime, YouthCouncil owningYouthCouncil, boolean isActive) {
		this.title = title;
		this.description = description;
		this.announcementTime = announcementTime;
		this.owningYouthCouncil = owningYouthCouncil;
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
