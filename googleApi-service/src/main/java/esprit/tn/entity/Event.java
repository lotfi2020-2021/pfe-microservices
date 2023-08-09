package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.EventAttachment;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String[] recurrence;
    private String summary;
    //private String location;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateTime;
    private String timeZone;
    private String[] attendees;
   // private String[] reminderMethods;
   // private int[] reminderMinutes;
    private Boolean isAddVideoConference ;
    private String meetUrl ;
    private String colorId ;
    private Boolean guestsCanModify;
    private Boolean attendeesOmitted ;
    private Boolean guestsCanInviteOthers;
    private Boolean guestsCanSeeOtherGuests;
   // private String transparency;
    private String visibility;

    private String googleEventId ;
    private String googleCalenderId ;
    private String creator ;

    @ManyToOne
    private Calendar calender ;
   // private Boolean locked;
    //private Boolean privateCopy;
   // private String status;
   // private Boolean anyoneCanAddSelf;

}
