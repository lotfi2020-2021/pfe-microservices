package esprit.tn.repository;


import esprit.tn.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DepartmentRepository extends JpaRepository<Department, Long > {

    @Query("SELECT d FROM Department d WHERE d.entreprise.id = :userId")
    List<Department> findByEntrepriseId(Long userId);


}
