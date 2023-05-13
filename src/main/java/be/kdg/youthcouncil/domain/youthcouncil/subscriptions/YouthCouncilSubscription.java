package be.kdg.youthcouncil.domain.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table (name = "youth_council_subscriptions", uniqueConstraints = {@UniqueConstraint (columnNames = {"subscriber", "youth_council"})})
@SQLDelete (sql = "UPDATE youth_council_subscriptions SET deleted = true WHERE youth_council_subscriptions.youth_council_subscription_id=?")
@Where (clause = "deleted = false")
public class YouthCouncilSubscription {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long youthCouncilSubscriptionId;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "subscriber")
	private PlatformUser subscriber;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "youth_council", nullable = false)
	private YouthCouncil youthCouncil;

	private SubscriptionRole role;

	@Column (name = "is_blocked", nullable = false)
	private boolean isBlocked = false;

	private boolean deleted = false;

	public YouthCouncilSubscription(PlatformUser subscriber, YouthCouncil youthCouncil, SubscriptionRole role) {
		this.subscriber = subscriber;
		this.youthCouncil = youthCouncil;
		this.role = role;
	}
}
