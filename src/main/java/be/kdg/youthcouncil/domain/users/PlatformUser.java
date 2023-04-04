package be.kdg.youthcouncil.domain.users;

import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointShare;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaShare;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.BlockedUser;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "platform_users")
public class PlatformUser implements Authenticable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Getter (AccessLevel.NONE)
	private long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String postcode;
	@Getter (AccessLevel.NONE)
	@Column (unique = true)
	private String username;
	@Getter (AccessLevel.NONE)
	private String password;
	@Getter (AccessLevel.NONE)
	private AuthenticationType authenticationType;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<ActionPointReaction> actionPointReactions;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<ActionPointShare> actionPointShares;

	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<IdeaReaction> ideaReactions;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<IdeaShare> ideaShares;
	@OneToMany (fetch = FetchType.LAZY)
	@JoinColumn (name = "subscriber")
	@ToString.Exclude
	private List<YouthCouncilSubscription> youthCouncilSubscriptions;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<ActionPointSubscription> actionPointSubscriptions;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<BlockedUser> blockedYouthCouncils;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Idea> ideas;

	public PlatformUser(String firstName, String lastName, String email, String postcode, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.postcode = postcode;
		this.username = username;
		this.password = password;
	}

	@Override
	public long getId() {
		return userId;
	}

	@Override
	public boolean isGA() {
		return false;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		PlatformUser that = (PlatformUser) o;
		return Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
