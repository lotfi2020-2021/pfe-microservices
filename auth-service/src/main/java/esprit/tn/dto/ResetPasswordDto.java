package esprit.tn.dto;

import  esprit.tn.annotation.PasswordRepeatEqual;
import  esprit.tn.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordRepeatEqual(
        passwordFieldFirst = "password",
        passwordFieldSecond = "passwordRepeat"
)
public class ResetPasswordDto {
    @ValidPassword
    private String password;
    private String passwordRepeat;
}
