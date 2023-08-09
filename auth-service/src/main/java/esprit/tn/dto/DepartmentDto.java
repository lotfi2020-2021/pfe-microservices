package esprit.tn.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;


    private String name;


    private String description;


    private String responsableDepartment;

    private Integer telephone;


    private String email;

    private Long entrepiseId ;


}
