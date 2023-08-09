package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.Key;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimeZone;

@Data
@Entity
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String timeZone;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String googleCalenderId;

    @JsonIgnore
    @OneToMany(mappedBy = "calender" , cascade = CascadeType.REMOVE)
     List<Event> events ;

}