package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String body ;


    public Long sender;

    public Long reciever ;


    public Boolean isRead;

    public String chatroomId ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    }
