package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.interfaces.Activatable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "informative_pages", uniqueConstraints = {@UniqueConstraint (columnNames = {"title", "owning_youth_council_youth_council_id"})})
public class InformativePage implements Activatable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long informativePageId;
	private String title;

	@ManyToOne
	@JoinColumn (name = "owning_youth_council_youth_council_id")
	private YouthCouncil owningYouthCouncil;

	@ManyToOne
	private InformativePage defaultInformativePage;

	@OneToMany (fetch = FetchType.EAGER)
	private List<InformativePageBlock> infoPageBlocks = new ArrayList<>();

	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	private boolean isActive;
	private boolean isDefault = false;

	private boolean isDisplayed;

	public InformativePage(String title, boolean isDefault) {
		this.title = title;
		this.isDefault = isDefault;
	}

	public InformativePage(String title, boolean isDefault, YouthCouncil owningYouthCouncil) {
		this.title = title;
		this.isDefault = isDefault;
		this.owningYouthCouncil = owningYouthCouncil;
	}

	public void addBlock(InformativePageBlock infoPageBlock) {
		this.infoPageBlocks.add(infoPageBlock);
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
