package be.kdg.youthcouncil.domain.youthcouncil.subscriptions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "blocked_users", uniqueConstraints = {@UniqueConstraint (columnNames = {"blocked_user", "blocked_from"})})
public class BlockedUser {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long blockedUserId;

	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "blocked_user", nullable = false)
	private PlatformUser blockedUser;
	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "blocked_from", nullable = false)
	private YouthCouncil youthCouncilBlockedFrom;
}
