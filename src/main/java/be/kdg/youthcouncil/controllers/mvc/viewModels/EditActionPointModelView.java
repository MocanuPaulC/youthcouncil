package be.kdg.youthcouncil.controllers.mvc.viewModels;

import be.kdg.youthcouncil.domain.moduleItems.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditActionPointModelView {

	private String title;
	private String description;
	private boolean isEnabled;
	private boolean isDefault;
	private Label label;

}
