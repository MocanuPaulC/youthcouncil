package be.kdg.youthcouncil.controllers.mvc.viewModels;


import be.kdg.youthcouncil.domain.moduleItems.Announcement;
import be.kdg.youthcouncil.domain.moduleItems.ModuleItem;
import be.kdg.youthcouncil.domain.user.User;
import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import lombok.*;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class YouthCouncilCreateModel {

    @NotBlank(message = "Council Name is required")
    @Size(min=8, max=100, message = "Council Name should have length between 8 and 100")
    private String councilName;
    @NotBlank(message = "Municipality name is required")
    @Size(min=8, max=100, message = "Municipality name should have length between 8 and 100")
    private String municipalityName;

    //TODO add default description
    private String description = "";

    //TODO add default council Logo
    private String councilLogo = "";
    private boolean isAfterElection = false;

}
