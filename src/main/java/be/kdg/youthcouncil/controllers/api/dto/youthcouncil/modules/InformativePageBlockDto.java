package be.kdg.youthcouncil.controllers.api.dto.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.enums.BlockType;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InformativePageBlockDto {
	private int orderNumber;
	@NotEmpty (message = "Blocks need to contain content, please remove them if they are empty!")
	@Size (max = 10000, message = "The content cannot size cannot exceed 10.000 letters and must be at least 1 letters long.")
	private String content;
	private BlockType type;

}
