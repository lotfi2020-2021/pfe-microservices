package esprit.tn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import esprit.tn.entity.Country;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String intro;
    private String gender;
    private String hometown;
    private String currentCity;
    private String eduInstitution;
    private String workplace;
    private String profilePhoto;
    private Date dateLastModified ;
    private String role;
    private Long telephone;
    private Boolean enabled;
    private Boolean accountVerified;
    private Boolean emailVerified;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;
    private Country country;
    private Long groupId;
    private Long entrepriseId ;
}
