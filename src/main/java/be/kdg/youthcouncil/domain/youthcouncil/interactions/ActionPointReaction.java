package be.kdg.youthcouncil.domain.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;

import javax.persistence.*;

@Entity
@Table (name = "action_point_reactions", uniqueConstraints = {@UniqueConstraint (columnNames = {"action_point_reacted_on", "reacting_user"})})
public class ActionPointReaction {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long reactionId;

	@ManyToOne
	@JoinColumn (name = "action_point_reacted_on", nullable = false)
	private ActionPoint actionPointReactedOn;
	private Emoji reaction;
	@ManyToOne
	@JoinColumn (name = "reacting_user", nullable = false)
	private PlatformUser reactingUser;

}
