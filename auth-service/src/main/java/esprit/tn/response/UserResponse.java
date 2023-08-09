package esprit.tn.response;

import esprit.tn.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User user;
   private String message ;
}
