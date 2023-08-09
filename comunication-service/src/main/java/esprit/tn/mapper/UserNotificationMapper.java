package esprit.tn.mapper;

import esprit.tn.dto.UserNotificationDto;

import esprit.tn.entity.UserNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserNotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "receiver", target = "receiverId")
    @Mapping(source = "notification.sender", target = "senderId")
    @Mapping(source = "notification", target = "notification")
    @Mapping(source = "isSeen", target = "isSeen")


    UserNotificationDto userNotificationToUserNotificationDto(UserNotification userNotification) ;


    List<UserNotificationDto> userNotificationsToUserNotificationsDtos(List<UserNotification> notifications);
}
