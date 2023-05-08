package be.kdg.youthcouncil.domain.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table (name = "info_page_blocks")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InformativePageBlock {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long blockId;
	private BlockType type;
	private int orderNumber;
	@Column (length = 10000)
	private String content;

	@ManyToOne
	@ToString.Exclude
	private InformativePage owningInformativePage;

	public InformativePageBlock(int orderNumber, String content, BlockType type) {
		this.orderNumber = orderNumber;
		this.content = content;
		this.type = type;
	}
}
