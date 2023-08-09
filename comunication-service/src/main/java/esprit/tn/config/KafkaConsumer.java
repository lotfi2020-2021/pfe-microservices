package esprit.tn.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import esprit.tn.dto.ChatDto;
import esprit.tn.dto.NotificationDto;
import esprit.tn.entity.Notification;
import esprit.tn.exception.MapperException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Component
public class KafkaConsumer {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    SimpMessagingTemplate template;



    @KafkaListener(topics = "notification", groupId = "notification-group-id", containerFactory = "kafkaListenerContainerFactory")
    public void listenSenderNotification(String data) {

        NotificationDto notification = fromJson(data, NotificationDto.class);
        log.info("Consumed message notification: " + data);
        template.convertAndSend("/topic/notif", notification);

    }


    @KafkaListener(topics = "chat", groupId = "chat-group-id", containerFactory = "kafkaListenerContainerFactory2")
    public void listenSendermessage(String data) {

        ChatDto chat = fromJson(data, ChatDto.class);
        log.info("Consumed message chat: " + data);
        template.convertAndSend("/topic/message", chat);

    }



    /**
     * Convert json to Object
     * @param json String json value
     * @param clazz Instances of the class
     * @param <T> Object Class
     * @return Object class
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
