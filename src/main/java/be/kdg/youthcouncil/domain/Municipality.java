package be.kdg.youthcouncil.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "municipalities")
public class Municipality {
	@Id
	@Column (name = "id", nullable = false)
	private Long id;
	private String name;
}
