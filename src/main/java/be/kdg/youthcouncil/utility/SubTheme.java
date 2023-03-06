package be.kdg.youthcouncil.utility;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "subthemes")
public class SubTheme {
    private String subTheme;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtheme_id")
    private long id;

}
