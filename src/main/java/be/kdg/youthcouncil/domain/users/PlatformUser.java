package be.kdg.youthcouncil.domain.users;

import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.ActionPointShare;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaReaction;
import be.kdg.youthcouncil.domain.youthcouncil.interactions.IdeaShare;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.utility.Notification;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "platform_users")
@SQLDelete (sql = "UPDATE platform_users SET deleted = true WHERE platform_users.user_id=?")
@Where (clause = "deleted = false")
public class PlatformUser implements Authenticable {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Getter (AccessLevel.NONE)
	private long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String postcode;
	private boolean isGA = false;
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
	@OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn (name = "subscriber")
	@ToString.Exclude
	private List<YouthCouncilSubscription> youthCouncilSubscriptions = new ArrayList<>();
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	@JoinColumn (name = "subscriber")
	private List<ActionPointSubscription> actionPointSubscriptions;
	@OneToMany (fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Idea> ideas;

	@OneToMany (fetch = FetchType.LAZY)
	private List<Notification> notifications;

	private boolean deleted = Boolean.FALSE;


	public PlatformUser(String firstName, String lastName, String email, String postcode, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.postcode = postcode;
		this.username = username;
		this.password = password;
		this.notifications = new ArrayList<>();
	}

	@Override
	public long getId() {
		return userId;
	}

	@Override
	public boolean isGA() {
		return isGA;
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

	public void addNotification(Notification notification) {
		notifications.add(notification);
	}

	public void addYouthCouncilSubscription(YouthCouncilSubscription subscription) {
		youthCouncilSubscriptions.add(subscription);
	}
}
