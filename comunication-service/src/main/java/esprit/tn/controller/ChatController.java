package esprit.tn.controller;

import esprit.tn.dto.ChatDto;
import esprit.tn.entity.Chat;
import esprit.tn.entity.Chatroom;
import esprit.tn.mapper.ChatMapper;
import esprit.tn.repository.ChatRepository;
import esprit.tn.service.ChatServiceImpl;
import esprit.tn.service.ChatroomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/chat")
public class ChatController {


    @Autowired
    private ChatServiceImpl messageService;

    @Autowired
    ChatRepository chatRepository ;
    @Autowired
    private ChatroomServiceImpl chatroomService;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private ChatServiceImpl chatService;

    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatDto>> getChatMessages(@PathVariable Long senderId,
                                                         @PathVariable Long recipientId) {
        List<Chat> messagesFromSenderRecipient = null;

            List<Chat> msgs = messageService.findChatMessagesFromSelectedUser(senderId, recipientId);

            Chatroom cr = chatroomService.findChatroomBySenderIdAndRecipientId(senderId, recipientId);

        List<ChatDto> messagesFromSenderRecipientDto = chatMapper.chatsToChatsDTO(msgs);
        return new ResponseEntity<List<ChatDto>>(messagesFromSenderRecipientDto, HttpStatus.OK);
    }



    @GetMapping("/latest-messages/{userId}")
    public ResponseEntity<List<ChatDto>> getLatestMessagesForAllChatrooms(@PathVariable("userId") Long userId) {

        List<Chat> latestMessages = chatService.getLatestMessagesForAllChatrooms(userId);
        List<ChatDto> latestMessagesDto =   chatMapper.chatsToChatsDTO(latestMessages) ;
        return ResponseEntity.ok().body(latestMessagesDto);
    }






    @PostMapping("/mark-as-seen/{receiverId}")
    public void markChatsAsSeen(@PathVariable("receiverId") Long receiverId) {

        chatService.markChatsAsSeen(receiverId);
    }


}
