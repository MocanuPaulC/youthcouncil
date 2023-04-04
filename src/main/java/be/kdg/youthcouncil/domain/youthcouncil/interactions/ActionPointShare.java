package be.kdg.youthcouncil.domain.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "action_point_shares", uniqueConstraints = {@UniqueConstraint (columnNames = {"action_point_shared", "sharing_user"})})
public class ActionPointShare {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long actionPointShareId;

	@ManyToOne
	@JoinColumn (name = "action_point_shared", nullable = false)
	private ActionPoint sharedActionPoint;

	@OneToOne
	private Image image;

	@ManyToOne
	@JoinColumn (name = "sharing_user", nullable = false)
	private PlatformUser platformUser;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ActionPointShare that = (ActionPointShare) o;
		return Objects.equals(getActionPointShareId(), that.getActionPointShareId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
