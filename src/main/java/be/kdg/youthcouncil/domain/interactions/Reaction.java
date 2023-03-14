package be.kdg.youthcouncil.domain.interactions;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table (name = "reactions")
public class Reaction {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "reaction_id")
	private long id;
	@Enumerated (EnumType.STRING)
	private Emoji emoji;
 
	public Reaction(Emoji emoji) {
		this.emoji = emoji;
	}

	public Reaction() {
	}
}
