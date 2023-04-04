package be.kdg.youthcouncil.domain.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "action_point_subscriptions", uniqueConstraints = {@UniqueConstraint (columnNames = {"subscriber", "action_point"})})
public class ActionPointSubscription {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long actionSubscriptionId;

	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "subscriber", nullable = false)
	private PlatformUser subscriber;

	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "action_point", nullable = false)
	private ActionPoint actionPoint;

	public ActionPointSubscription(PlatformUser subscriber, ActionPoint actionPoint) {
		this.subscriber = subscriber;
		this.actionPoint = actionPoint;
	}
}
