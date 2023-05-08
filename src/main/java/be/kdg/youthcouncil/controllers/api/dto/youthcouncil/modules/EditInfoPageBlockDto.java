package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EditInfoPageBlockDto {
	private long blockId;
	private int orderNumber;
	private String content;
	private BlockType type;
}
