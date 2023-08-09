package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 8)
    private Long telephone;


    private Boolean valid;

    @Column(length = 128, nullable = false)
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;



}
