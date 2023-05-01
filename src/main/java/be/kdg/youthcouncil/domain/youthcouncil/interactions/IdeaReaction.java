package be.kdg.youthcouncil.domain.youthcouncil.interactions;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.domain.youthcouncil.modules.Idea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name = "idea_reactions", uniqueConstraints = {@UniqueConstraint (columnNames = {"idea_reacted_on", "reacting_user"})})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IdeaReaction {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long reactionId;

	@ManyToOne
	@JoinColumn (name = "idea_reacted_on", nullable = false)
	private Idea ideaReactedOn;
	private Emoji reaction;
	@ManyToOne
	@JoinColumn (name = "reacting_user", nullable = false)
	private PlatformUser reactingUser;

}
