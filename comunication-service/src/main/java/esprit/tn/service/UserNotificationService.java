package esprit.tn.service;

import esprit.tn.entity.UserNotification;

import java.util.List;

public interface UserNotificationService {


  List<UserNotification> getNotificatinReceiversBynotificationAndByUser(Long notificationId, Long userId) ;

   void markAllSeen(Long userId);

    void deleteUserNotification(Long notificationId);

    List<UserNotification> getNotificationsForAuthUser( Long id);
     void markNotificationAsRead(Long notificationId) ;

     UserNotification getUserNotificationById(Long id) ;

}
