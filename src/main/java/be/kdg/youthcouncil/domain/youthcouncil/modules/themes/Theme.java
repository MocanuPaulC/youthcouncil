package be.kdg.youthcouncil.domain.youthcouncil.modules.themes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "themes")
@ToString
public class Theme {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long themeId;
	private String theme;
	@OneToMany (fetch = FetchType.EAGER)
	@JoinColumn (name = "super_theme_theme_id")
	@ToString.Exclude
	private List<SubTheme> subThemes;

	public Theme(String theme) {
		this.theme = theme;
	}
}
