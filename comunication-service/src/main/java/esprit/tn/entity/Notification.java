package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import esprit.tn.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notification")
@Data
 @ToString
 @AllArgsConstructor
 @NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
     private NotificationType type;


    @JoinColumn(name = "sender_id")
    private Long sender;


    @OneToMany(mappedBy = "notification" ,cascade = CascadeType.ALL)
    private List<UserNotification> receivers;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateUpdated;




}

