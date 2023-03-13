package be.kdg.youthcouncil.domain.interactions;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table (name = "shares")
public class Share {


	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "share_id")
	private long id;

}
