package be.kdg.youthcouncil.domain.youthcouncil;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Announcement;
import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.ActionPointNotFoundException;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table (name = "youth_councils")
public class YouthCouncil {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long youthCouncilId;
	private String councilName;
	private String municipality;
	private String description;
	@OneToOne
	private Image councilLogo;
	private boolean isAfterElection;
	@OneToMany (fetch = FetchType.LAZY)
	@JoinColumn (name = "youth_council")
	@ToString.Exclude
	private List<YouthCouncilSubscription> subscriptions;
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "owningYouthCouncil")
	@ToString.Exclude
	private List<Announcement> announcements;
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "owningYouthCouncil")
	@ToString.Exclude
	private List<InformativePage> informativePages;
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "owningYouthCouncil")
	@ToString.Exclude
	private List<ActionPoint> actionPoints;
	@OneToMany (mappedBy = "owningYouthCouncil", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<CallForIdea> callForIdeas;

	public YouthCouncil(String councilName, String municipality, String description, Image councilLogo, boolean isAfterElection) {
		this.councilName = councilName;
		this.municipality = municipality;
		this.description = description;
		this.councilLogo = councilLogo;
		this.isAfterElection = isAfterElection;
	}

	public void addInformativePage(InformativePage informativePage) {
		informativePages.add(informativePage);
	}

	public ActionPoint getActionPoint(long id) {
		return actionPoints.stream()
		                   .filter(item -> item.getActionPointId() == id)
		                   .findFirst()
		                   .orElseThrow(() -> {throw new ActionPointNotFoundException(id);});
	}

	public void addActionPoint(ActionPoint newActionPoint) {
		actionPoints.add(newActionPoint);
	}
}
