package esprit.tn.response;

import esprit.tn.entity.Notification;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse{

    private List<String> emailList;
    private Notification notification;
}
