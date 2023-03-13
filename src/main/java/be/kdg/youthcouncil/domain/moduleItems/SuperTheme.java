package be.kdg.youthcouncil.domain.moduleItems;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table (name = "themes")
public class SuperTheme {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "theme_id")
	private long id;

	@OneToMany
	@ToString.Exclude
	private List<SubTheme> subThemes;

	private String theme;
}
