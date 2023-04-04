package be.kdg.youthcouncil.domain.youthcouncil.modules.themes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sub_themes")
public class SubTheme {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long subThemeId;
	private String subTheme;
	@ManyToOne
	private Theme superTheme;

	public SubTheme(String subTheme, Theme superTheme) {
		this.subTheme = subTheme;
		this.superTheme = superTheme;
	}
}
