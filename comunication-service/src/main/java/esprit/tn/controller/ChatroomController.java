package esprit.tn.controller;

import esprit.tn.dto.ChatDto;
import esprit.tn.entity.Chat;
import esprit.tn.entity.Chatroom;
import esprit.tn.mapper.ChatMapper;
import esprit.tn.mapper.ChatroomMapper;
import esprit.tn.repository.ChatRepository;
import esprit.tn.repository.ChatRoomRepository;
import esprit.tn.service.ChatServiceImpl;
import esprit.tn.service.ChatroomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v2/chatroom")
public class ChatroomController {



    @Autowired
    ChatroomServiceImpl  chatroomServiceImpl ;





    @Autowired
    private ChatroomMapper chatroomMapper;
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatRepository messageRepository;

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private ChatRoomRepository chatroomRepository;

    @PostMapping("/chat/{senderId}/{receiverId}")
    public  ResponseEntity<?> sendMessage( @RequestBody Chat chatMessage,
          @PathVariable Long senderId , @PathVariable Long receiverId) {



      Chatroom chatroom = chatroomRepository.findChatroomBySenderIdAndRecipientId(senderId,receiverId);

        String chatroomId = "";
        if(chatroom ==null){
            chatroomId= String.format("%d_%d", senderId, receiverId);



            Chatroom senderRecipient = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .sender(senderId)
                    .created(new Date())
                    .reciever(receiverId)
                    .build();

            Chatroom recipientSender = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .sender(receiverId)
                    .reciever(senderId)
                    .created(new Date())
                    .build();
            try{
                chatroomRepository.save(senderRecipient);
                chatroomRepository.save(recipientSender);
            }
            catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException("Cannont create new chat room between sender "+senderId+" and recipient "+receiverId);
            }

        }
        else{
            chatroomId = chatroom.getChatroomId();
        }
        chatMessage.setCreated(new Date());
        chatMessage.setChatroomId(chatroomId);
        chatMessage.setIsRead(false);
        chatMessage.setReciever(receiverId);
        chatMessage.setSender(senderId);
        chatMessage.setBody(chatMessage.getBody());
        Chat saved = null;
        try{
            saved = messageRepository.save(chatMessage);

        }
        catch(Exception ex){
            throw new RuntimeException("Cannot create new message in chatroomId "+ chatroomId);
        }

        ChatDto chatDto = chatMapper.chatToChatDTO(saved);

      chatroomServiceImpl.send(chatDto);
        return  new ResponseEntity(chatDto,HttpStatus.OK);
    }







}
