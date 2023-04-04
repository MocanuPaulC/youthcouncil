package be.kdg.youthcouncil.domain.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.media.Image;
import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
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
@Table (name = "idea_shares", uniqueConstraints = {@UniqueConstraint (columnNames = {"idea_shared", "sharing_user"})})
public class IdeaShare {


	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long ideaShareId;

	@ManyToOne
	@JoinColumn (name = "idea_shared", nullable = false)
	private Idea sharedIdea;

	@OneToOne
	private Image image;

	@ManyToOne
	@JoinColumn (name = "sharing_user", nullable = false)
	private PlatformUser platformUser;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		IdeaShare ideaShare = (IdeaShare) o;
		return Objects.equals(getIdeaShareId(), ideaShare.getIdeaShareId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
