package esprit.tn.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String responsableDepartment;

    private Integer telephone;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private User entreprise;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Group> groups;
}

