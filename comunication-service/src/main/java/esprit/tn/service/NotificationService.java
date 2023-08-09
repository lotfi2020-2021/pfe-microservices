package esprit.tn.service;


import esprit.tn.dto.NotificationDto;
import esprit.tn.entity.Notification;

import java.util.List;

public interface NotificationService {

    /**
     * Send notification
     * @param notifications model of notification
     */
    void send(NotificationDto notifications);

    Notification save(Notification notification ,List<Long> users ,Long sender) ;

    List<Notification> getNotifications();

    Notification getNotificationById(Long idNotification);

   // Notification update(Notification notification, List<Long> users, Long notificationId);

    void deleteNotification(Long id);



}
