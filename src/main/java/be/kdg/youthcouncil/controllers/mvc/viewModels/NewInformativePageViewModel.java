package be.kdg.youthcouncil.controllers.mvc.viewModels;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewInformativePageViewModel {

    @NotBlank(message = "Title is required")
    @Size(min=8, max=100, message = "Title should have length between 8 and 100")
    private String title;

    //TODO this does not check correctly
    @NotNull(message = "Description is required")
    private List<String> paragraphs;


    private List<String> images = new ArrayList<>();

    private List<String> videos = new ArrayList<>();

}
