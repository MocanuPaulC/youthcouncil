package be.kdg.youthcouncil.domain.media;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue ("Image")
public class Image extends Media {


	public Image(String path) {
		this.path = path;
	}

	@Override
	public MediaType type() {
		return MediaType.IMAGE;
	}
}
