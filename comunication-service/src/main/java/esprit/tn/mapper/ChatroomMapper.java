package esprit.tn.mapper;

import esprit.tn.dto.ChatroomDto;
import esprit.tn.entity.Chatroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface ChatroomMapper {



    @Mapping( source = "sender",target = "senderId")
    @Mapping( source = "reciever",target = "recieverId")

    @Mapping( source = "created",target = "created")
    ChatroomDto chatroomToChatroomDTO(Chatroom chat);

    List<ChatroomDto> chatroomsToChatroomsDTO(List<Chatroom> chats);

}


