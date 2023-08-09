package esprit.tn.mapper;

import esprit.tn.dto.NotificationDto;

import esprit.tn.entity.Notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping( source = "id",target = "id")
    @Mapping( source = "content",target = "content")
    @Mapping( source = "title",target = "title")
    @Mapping( source = "sender",target = "sender")
    @Mapping( source = "dateUpdated",target = "dateUpdated")
    @Mapping( source = "dateCreated",target = "dateCreated")
    NotificationDto notificationToNotificationDto(Notification notification);

    List<NotificationDto> notificationsToNotificationsDto(List<Notification> notifications);
}
