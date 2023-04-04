package be.kdg.youthcouncil.domain.media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "media")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "media_type_discr", discriminatorType = DiscriminatorType.STRING)
public abstract class Media {

	protected String path;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long mediaId;

	public String getPath() {
		return path;
	}

	abstract MediaType type();
}
