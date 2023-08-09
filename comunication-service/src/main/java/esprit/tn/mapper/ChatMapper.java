package esprit.tn.mapper;

import esprit.tn.dto.ChatDto;

import esprit.tn.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface ChatMapper {


    @Mapping(source = "body",target = "body")
    @Mapping( source = "sender",target = "senderId")
    @Mapping( source = "reciever",target = "recieverId")

    @Mapping( source = "isRead",target = "isRead")
    @Mapping( source = "chatroomId",target = "chatroomId")
    @Mapping( source = "created",target = "created")
    ChatDto chatToChatDTO(Chat chat);

    List<ChatDto> chatsToChatsDTO(List<Chat> chats);
}


