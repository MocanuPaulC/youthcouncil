package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.media.Media;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "announcements")
public class Announcement {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long announcementId;
	private String title;
	private String description;
	private LocalDateTime announcementTime;
	@OneToMany (fetch = FetchType.LAZY)
	private List<Media> media;
	@ManyToOne
	private YouthCouncil owningYouthCouncil;
	private ModuleStatus moduleStatus;

	public Announcement(String title, String description, LocalDateTime announcementTime, YouthCouncil owningYouthCouncil, ModuleStatus moduleStatus) {
		this.title = title;
		this.description = description;
		this.announcementTime = announcementTime;
		this.owningYouthCouncil = owningYouthCouncil;
		this.moduleStatus = moduleStatus;
	}

}
