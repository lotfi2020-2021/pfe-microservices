package esprit.tn.repository;

import esprit.tn.entity.*;


import esprit.tn.enumeration.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);



  void deleteByEmail(String email);

List<User> findByEntrepriseId (Long id) ;



    User findUserById(Long id);

    @Query("SELECT d FROM User d WHERE d.group.id = :id")
    List<User> findusersByGroupId(Long id);
    List<User> findByRole(Role role);


    @Query("SELECT u FROM User u LEFT JOIN u.group g WHERE g.id IS NULL AND u.role = 'user'")
    List<User> findUsersWithoutGroupId();


    @Query("SELECT u FROM User u WHERE u.entreprise.id = :entrepriseId")
    List<User> findUsersByEntrepriseId(@Param("entrepriseId") Long entrepriseId);

//    @Query("SELECT u FROM User u WHERE u.entreprise.id = :entrepriseId AND u.id <> :currentUserId " +
//            "AND NOT EXISTS (SELECT m FROM Chat m WHERE (m.sender = u AND m.reciever.id = :currentUserId) " +
//            "OR (m.sender.id = :currentUserId AND m.reciever = u))")
//    List<User> findUsersByEntrepriseIdWithoutChatroom(@Param("entrepriseId") Long entrepriseId ,@Param("currentUserId") Long currentUserId );

//    @Query("SELECT DISTINCT u FROM User u JOIN Chat m ON (m.sender.id = u.id OR m.reciever.id = u.id) " +
//            "WHERE u.entreprise.id = :entrepriseId AND u.id <> :currentUserId")
//    List<User> findUsersByEntrepriseIdWithoutChatroom(@Param("entrepriseId") Long entrepriseId, @Param("currentUserId") Long currentUserId);
//


}
