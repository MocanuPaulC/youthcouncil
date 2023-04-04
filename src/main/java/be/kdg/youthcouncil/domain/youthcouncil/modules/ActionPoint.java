package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.media.Video;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointShare;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "action_points")
public class ActionPoint implements Activatable, Defaultable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long actionPointId;

	@Column (nullable = false)
	private String title;
	private String description;
	@ManyToOne
	@JoinColumn (nullable = false)
	private SubTheme theme;
	@OneToMany (fetch = FetchType.LAZY)
	private List<Image> images;
	@OneToOne
	private Video video;

	@Column (nullable = false)
	private ActionPointStatus status;
	@OneToMany (fetch = FetchType.LAZY)
	private List<ActionPointSubscription> subscriptions;
	@OneToMany (fetch = FetchType.LAZY)
	private List<Idea> inspiredBy;
	@ManyToOne
	@JoinColumn (nullable = false)
	private YouthCouncil owningYouthCouncil;

	@OneToMany (fetch = FetchType.LAZY)
	private List<ActionPointShare> shares;

	@OneToMany (fetch = FetchType.LAZY)
	private List<ActionPointReaction> reactions;

	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isActive;
	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isDefault;

	public ActionPoint(String title, String description, SubTheme theme, ActionPointStatus status, YouthCouncil owningYouthCouncil, boolean isActive) {
		this.title = title;
		this.description = description;
		this.theme = theme;
		this.status = status;
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

	@Override
	public boolean getIsDefault() {
		return isDefault;
	}

	@Override
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ActionPoint that = (ActionPoint) o;
		return actionPointId == that.actionPointId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actionPointId);
	}
}
