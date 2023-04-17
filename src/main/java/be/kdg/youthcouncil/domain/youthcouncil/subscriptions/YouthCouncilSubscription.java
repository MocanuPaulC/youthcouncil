package be.kdg.youthcouncil.domain.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "youth_council_subscriptions", uniqueConstraints = {@UniqueConstraint (columnNames = {"subscriber", "youth_council"})})
public class YouthCouncilSubscription {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long youthCouncilSubscriptionId;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "subscriber", nullable = false)
	private PlatformUser subscriber;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "youth_council", nullable = false)
	private YouthCouncil youthCouncil;

	private SubscriptionRole role;

	@Column (name = "is_blocked", nullable = false)
	private boolean isBlocked = false;

	public YouthCouncilSubscription(PlatformUser subscriber, YouthCouncil youthCouncil, SubscriptionRole role) {
		this.subscriber = subscriber;
		this.youthCouncil = youthCouncil;
		this.role = role;
	}
}
