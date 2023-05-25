package be.kdg.youthcouncil.domain.youthcouncil.modules.themes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table (name = "sub_themes")
public class SubTheme {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long subThemeId;
	private String subTheme;

	@ToString.Exclude
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "super_theme_theme_id", nullable = false)
	private Theme superTheme;

	public SubTheme(String subTheme, Theme superTheme) {
		this.subTheme = subTheme;
		this.superTheme = superTheme;
	}
}
