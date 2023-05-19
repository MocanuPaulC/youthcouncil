package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table (name = "action_point_blocks")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActionPointBlock {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long blockId;
	private BlockType type;
	private int orderNumber;
	@Column (length = 10000)
	private String content;

	@ManyToOne
	@ToString.Exclude
	private ActionPoint owningActionPoint;

	public ActionPointBlock(int orderNumber, String content, BlockType type) {
		this.orderNumber = orderNumber;
		this.content = content;
		this.type = type;
	}
}
