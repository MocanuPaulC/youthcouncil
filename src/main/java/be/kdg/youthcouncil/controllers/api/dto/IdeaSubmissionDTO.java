package be.kdg.youthcouncil.controllers.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class IdeaSubmissionDTO {
	private String title;
	private boolean toModerate = true;

	public IdeaSubmissionDTO(String title) {
		this.title = title;
	}
}
