package esprit.tn.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "userNotification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @JoinColumn(name = "receiver_id")
    private Long receiver;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private Boolean isSeen;


}
