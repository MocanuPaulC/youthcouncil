package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.Theme;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "call_for_ideas")
public class CallForIdea implements Defaultable, Activatable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long callForIdeaId;
	private String title;
	@OneToMany (fetch = FetchType.LAZY)
	private List<Idea> ideas;
	@ManyToOne
	private YouthCouncil owningYouthCouncil;
	@OneToOne
	private Theme theme;
	private boolean isClosed;
	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isActive;
	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isDefault;


	/*
	 * Use this constructor to create a specialized Call for ideas
	 */
	public CallForIdea(String title, YouthCouncil owningYouthCouncil, Theme theme, boolean isActive, boolean isClosed) {
		this.title = title;
		this.owningYouthCouncil = owningYouthCouncil;
		this.isActive = isActive;
		this.isDefault = false;
		this.isClosed = isClosed;
	}


	/*
	 * This constructor is supposed to be used to create Default Call for ideas!
	 */
	public CallForIdea(String title, boolean isActive, boolean isClosed) {
		this.title = title;
		this.owningYouthCouncil = null;
		this.isActive = isActive;
		this.isDefault = true;
		this.isClosed = isClosed;
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
}
