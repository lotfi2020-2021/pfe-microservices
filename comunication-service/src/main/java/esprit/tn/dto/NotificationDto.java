package esprit.tn.dto;

import esprit.tn.enumeration.NotificationType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {


    private Long id;
    private String title;
    private String content;
    private NotificationType type;
    private Long sender;
  //  private List<UserNotificationDto> receivers;
    private Date dateCreated;
    private Date dateUpdated;
}
