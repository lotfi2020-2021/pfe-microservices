package esprit.tn.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = true)
    private String description;

    @Column(length = 64, nullable = true)
    private String responsableGroup;

    @ManyToOne( cascade = CascadeType.DETACH)
    @JoinColumn(name = "department_id" )
    Department department ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.DETACH )
    private List<User> employees = new ArrayList<>();



}
