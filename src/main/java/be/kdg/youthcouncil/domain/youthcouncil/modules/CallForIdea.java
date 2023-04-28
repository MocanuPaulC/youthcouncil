package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.interfaces.Defaultable;
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
public class CallForIdea implements Defaultable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long callForIdeaId;
	private String title;
	@OneToMany (fetch = FetchType.LAZY)
	@JoinColumn (name = "call_for_ideas_call_for_idea_id")
	private List<Idea> ideas;
	@ManyToOne
	private YouthCouncil owningYouthCouncil;
	@OneToOne
	@JoinColumn (name = "theme_id")
	private Theme theme;
	private boolean isClosed;
	@Setter (AccessLevel.NONE)
	@Getter (AccessLevel.NONE)
	private boolean isDefault;

	private ModuleStatus moduleStatus;


	/*
	 * Use this constructor to create a specialized Call for ideas
	 */
	public CallForIdea(String title, YouthCouncil owningYouthCouncil, Theme theme, ModuleStatus moduleStatus, boolean isClosed) {
		this.title = title;
		this.owningYouthCouncil = owningYouthCouncil;
		this.isDefault = false;
		this.isClosed = isClosed;
		this.theme = theme;
		this.moduleStatus = moduleStatus;
	}


	/*
	 * This constructor is supposed to be used to create Default Call for ideas!
	 */
	public CallForIdea(String title, boolean isActive, boolean isClosed) {
		this.title = title;
		this.owningYouthCouncil = null;
		this.isDefault = true;
		this.isClosed = isClosed;
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
