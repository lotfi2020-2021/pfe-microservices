package esprit.tn.controller;

import esprit.tn.dto.UserNotificationDto;
import esprit.tn.entity.UserNotification;
import esprit.tn.mapper.UserNotificationMapper;
import esprit.tn.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v2/userNotification")
public class UserNotificationController {

    @Autowired
    UserNotificationService userNotificationService ;

    @Autowired
    UserNotificationMapper userNotificationMapper ;

    @GetMapping("/notifications/User/{userId}/{notificationId}")
    public ResponseEntity<?> GetNotificationsReceiversByUser(@PathVariable Long userId , @PathVariable Long notificationId) {
        List<UserNotification> list = userNotificationService.getNotificatinReceiversBynotificationAndByUser(notificationId ,userId);
        List<UserNotificationDto> userNotificationDtos =  userNotificationMapper.userNotificationsToUserNotificationsDtos(list) ;
        return new ResponseEntity<>(userNotificationDtos, HttpStatus.OK);
    }



        @GetMapping("/notifications/mark-seen/{userId}")
    public ResponseEntity<?> markAllSeen(@PathVariable Long userId) {
            userNotificationService.markAllSeen(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/notifications/{userId}")
    public ResponseEntity<?> GetNotificationsByReceiver( @PathVariable Long userId) {
        List<UserNotification> list = userNotificationService.getNotificationsForAuthUser(userId) ;
        List<UserNotificationDto> listDtos =  userNotificationMapper.userNotificationsToUserNotificationsDtos(list);
        return new ResponseEntity<>(listDtos, HttpStatus.OK);
    }


    @GetMapping("/notifications/read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        userNotificationService.markNotificationAsRead(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/notifications/get/{id}")
    public ResponseEntity<?> getUserNotificationById(@PathVariable Long id) {
        UserNotification userNotification = userNotificationService.getUserNotificationById(id);
        UserNotificationDto userNotificationDto=  userNotificationMapper.userNotificationToUserNotificationDto(userNotification) ;
        return new ResponseEntity<>( userNotificationDto, HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotification( @PathVariable Long id) {
        userNotificationService.deleteUserNotification(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
