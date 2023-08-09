package esprit.tn.service;

import esprit.tn.entity.Chat;
import esprit.tn.repository.ChatRepository;
import esprit.tn.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRoomRepository chatroomRepository;

    public List<Chat> findChatMessagesFromSelectedUser(Long senderId, Long recipientId) {
        return chatRepository.findChatMessagesFromSelectedUser(senderId, recipientId);
    }


    public List<Chat> findChatMessagesByChatroomId(String chatroomId) {
        return chatRepository.findChatMessagesByChatroomId(chatroomId);
    }


    public List<Chat> getLatestMessagesForAllChatrooms(Long user) {
        List<Chat> latestMessages = new ArrayList<>();
        List<String> chatroomIds = chatroomRepository.findAllDistinctBySender(user);

        for (String chatroomId : chatroomIds) {
            List<Chat> chats = chatRepository.findByChatroomIdOrderByCreatedDesc(chatroomId);
            if (!chats.isEmpty()) {
                latestMessages.add(chats.get(0));
            }
        }

        return latestMessages;
    }


    public void markChatsAsSeen(Long receiver) {
        List<Chat> chats = chatRepository.findChatsByRecieverAndIsReadIsFalse(receiver);
        for (Chat chat : chats) {
            chat.setIsRead(true);
            chatRepository.save(chat);
        }
    }


}
