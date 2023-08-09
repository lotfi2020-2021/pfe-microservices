package esprit.tn.repository;

import esprit.tn.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {


    @Query("SELECT n FROM UserNotification n WHERE n.notification.id = :notificationId AND n.notification.sender = :senderId")
    List<UserNotification> getNotificationBySenderIdAndNotificationId(@Param("notificationId") Long notificationId, @Param("senderId") Long senderId);




    List<UserNotification> findByNotificationId(Long id);




      List<UserNotification> findUserNotificationsByReceiver(Long receiver);
     List<UserNotification> findUserNotificationsByReceiverAndIsSeenIsFalse(Long receiver);



}
