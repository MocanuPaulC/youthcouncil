package be.kdg.youthcouncil.domain.interactions;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reactions")
public class Reaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private long id;

    public Reaction(Emoji emoji) {
        this.emoji = emoji;
    }
    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    public Reaction() {
    }
}
