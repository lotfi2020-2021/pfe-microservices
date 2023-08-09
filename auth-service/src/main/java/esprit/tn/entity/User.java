package esprit.tn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esprit.tn.enumeration.Gender;
import esprit.tn.enumeration.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    @Email
    private String email;


    @Column(length = 256, nullable = false)
    @JsonIgnore
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entreprise_id")
    private User entreprise;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 100)
    private String intro;

    @Column(length = 16)
    private String gender;

    @Column(length = 128)
    private String hometown;

    @Column(length = 128)
    private String currentCity;

    @Column(length = 128)
    private String eduInstitution;

    @Column(length = 128)
    private String workplace;

    @Column(length = 256)
    private String profilePhoto;




    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;

    @Column(length = 32, nullable = false)
    private Role role;
    @Column(length = 8)
    private Long telephone;

    private Boolean enabled;
    private Boolean accountVerified;
    private Boolean emailVerified;



    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne( cascade = CascadeType.DETACH )
    @JoinColumn(name = "group_id"  )
    private Group group;


    @OneToMany(mappedBy = "entreprise", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Department> departments = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
