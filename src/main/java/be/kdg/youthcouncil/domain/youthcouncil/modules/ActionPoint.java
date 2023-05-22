package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.media.Video;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointShare;
import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.ActionPointStatus;
import be.kdg.youthcouncil.domain.youthcouncil.modules.interfaces.Defaultable;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "action_points")
public class ActionPoint implements Defaultable {

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
	@JoinColumn (name = "action_point")
	private List<ActionPointSubscription> subscriptions;
	@OneToMany (fetch = FetchType.LAZY)
	private List<Idea> inspiredBy;
	@ManyToOne
	@JoinColumn (nullable = false)
	private YouthCouncil owningYouthCouncil;
	@OneToMany (fetch = FetchType.LAZY)
	private List<ActionPointShare> shares;
	@OneToMany (fetch = FetchType.LAZY)
	@JoinColumn (name = "action_point_reacted_on")
	private List<ActionPointReaction> reactions;
	private ModuleStatus moduleStatus;
	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isDefault;
	private boolean isDisplayed = false;

	@OneToMany (fetch = FetchType.EAGER)
	private List<ActionPointBlock> actionPointBlocks = new ArrayList<>();

	public ActionPoint(String title, SubTheme theme, boolean isDefault, YouthCouncil owningYouthCouncil) {
		this.title = title;
		this.isDefault = isDefault;
		this.theme = theme;
		this.owningYouthCouncil = owningYouthCouncil;
		this.status = ActionPointStatus.NEW;
		this.moduleStatus = ModuleStatus.DISPLAYED;
	}

	public ActionPoint(String title, String description, SubTheme theme, ActionPointStatus status, YouthCouncil owningYouthCouncil, ModuleStatus moduleStatus) {
		this.title = title;
		this.description = description;
		this.theme = theme;
		this.status = status;
		this.owningYouthCouncil = owningYouthCouncil;
		this.moduleStatus = moduleStatus;
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

	public void addReaction(ActionPointReaction reaction) {
		reactions.add(reaction);
	}

	public void addIdea(Idea idea) {
		inspiredBy.add(idea);
	}
}
