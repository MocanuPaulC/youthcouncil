package be.kdg.youthcouncil.domain.youthcouncil.modules.themes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "themes")
public class Theme {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long themeId;
	private String theme;
	@OneToMany(fetch = FetchType.LAZY)
	private List<SubTheme> subThemes;

	public Theme(String theme) {
		this.theme = theme;
	}
}
