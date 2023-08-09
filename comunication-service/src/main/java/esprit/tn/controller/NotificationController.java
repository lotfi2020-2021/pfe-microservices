package esprit.tn.controller;


import esprit.tn.client.ComunicatinClient;
import esprit.tn.dto.NotificationDto;
import esprit.tn.entity.Notification;
import esprit.tn.mapper.NotificationMapper;
import esprit.tn.response.NotificationResponse;
import esprit.tn.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v2/notification")

public class NotificationController {


    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
     ComunicatinClient comunicatinClient;
    @Autowired
    private  NotificationService notificationService;



    @PostMapping("/send/{userId}")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationResponse notificationResponse , @PathVariable Long userId) {
        List<Long> users = new ArrayList<>();
        for (String email : notificationResponse.getEmailList()) {
            Long receiver = comunicatinClient.getUserByEmail2(email);
            users.add(receiver);
        }


        Notification notification =  notificationService.save( notificationResponse.getNotification() , users , userId) ;
        NotificationDto notificationDto = notificationMapper.notificationToNotificationDto(notification);
        System.out.println("notifications" + notification);
        notificationService.send(notificationDto);
        return new ResponseEntity<>( notificationDto ,HttpStatus.OK);
    }


    @GetMapping("/notifications")
    public ResponseEntity<?> GetNotifications() {
       List<Notification> list = notificationService.getNotifications();
        List<NotificationDto> notificationsDtos = notificationMapper.notificationsToNotificationsDto(list) ;
        return new ResponseEntity<>(notificationsDtos, HttpStatus.OK);
    }





    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetNotificationById( @PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
     NotificationDto notificationDto =  notificationMapper.notificationToNotificationDto(notification) ;
        return new ResponseEntity<>(notificationDto, HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotification( @PathVariable Long id) {
       notificationService.deleteNotification(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


//    @PutMapping("/update/{notificationId}")
//    public ResponseEntity<NotificationDto> updateNotification(@PathVariable Long notificationId, @RequestBody NotificationResponse notificationResponse) {
//        List<User> users = new ArrayList<>();
//        for (String email : notificationResponse.getEmailList()) {
//            User receiver = userRepository.findByEmail(email).get();
//            users.add(receiver);
//        }
//        Notification updatedNotification = notificationService.update(notificationResponse.getNotification(), users, notificationId);
//     NotificationDto updatedNotificationDto = notificationMapper.notificationToNotificationDto(updatedNotification) ;
//        System.out.println("notifications" + updatedNotificationDto);
//        notificationService.send(updatedNotificationDto);
//        return new ResponseEntity<>( updatedNotificationDto ,HttpStatus.OK);
//    }


}
