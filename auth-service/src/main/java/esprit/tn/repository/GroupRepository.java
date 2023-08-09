package esprit.tn.repository;

import esprit.tn.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface GroupRepository extends JpaRepository<Group,Long > {


    Optional<Group> findByName(String name);
    @Query("SELECT d FROM Group d WHERE d.department.id = :idDepartment")
    List<Group> findAllByDepartmentId(Long idDepartment) ;
}
