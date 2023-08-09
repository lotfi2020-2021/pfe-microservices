package esprit.tn.dto;

import esprit.tn.annotation.ValidEmail;
import  esprit.tn.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
