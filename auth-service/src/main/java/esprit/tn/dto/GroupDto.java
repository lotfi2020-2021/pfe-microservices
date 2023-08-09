package esprit.tn.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {



    private Long id ;


    private String name;

    private String description ;

    private String responsableGroup;


    DepartmentDto department ;

        List<UserDto> employees;


}
