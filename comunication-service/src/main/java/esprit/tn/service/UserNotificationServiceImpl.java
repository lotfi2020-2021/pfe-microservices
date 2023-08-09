package esprit.tn.service;

import esprit.tn.entity.UserNotification;
import esprit.tn.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserNotificationServiceImpl implements  UserNotificationService{


    @Autowired
    UserNotificationRepository userNotificationRepository ;


    @Override
    public List<UserNotification> getNotificatinReceiversBynotificationAndByUser(Long notificationId, Long userId) {
        List<UserNotification> list =userNotificationRepository.getNotificationBySenderIdAndNotificationId(notificationId,userId);

        return list;
    }





    @Override
    public void markAllSeen(Long userId) {

        userNotificationRepository.findUserNotificationsByReceiverAndIsSeenIsFalse(userId)
                .forEach(notification -> {
                    if (notification.getReceiver().equals(userId)) {
                        notification.setIsSeen(true);
                        userNotificationRepository.save(notification);
                    }
                });
    }

    @Override
    public void deleteUserNotification(Long notificationId) {
        UserNotification userNotification = userNotificationRepository.findById(notificationId).get();
        userNotificationRepository.deleteById(notificationId);
    }


    @Override
    public List<UserNotification> getNotificationsForAuthUser(Long receiverId) {

            return userNotificationRepository.findUserNotificationsByReceiver(receiverId);
    }

     @Override
    public void markNotificationAsRead(Long notificationId) {
        UserNotification userNotification = userNotificationRepository.findById(notificationId).get();
        userNotification.setIsSeen(true);
        userNotificationRepository.save(userNotification);
    }

    @Override
    public UserNotification getUserNotificationById(Long id) {
        UserNotification userNotification = userNotificationRepository.findById(id).get();
        return userNotification ;
    }


}
