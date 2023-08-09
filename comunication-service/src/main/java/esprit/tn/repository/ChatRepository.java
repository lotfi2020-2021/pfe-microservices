package esprit.tn.repository;

import esprit.tn.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRepository extends JpaRepository<Chat , Long> {









    @Query("FROM Chat m WHERE m.chatroomId = :chatroomId")
    List<Chat> findChatMessagesByChatroomId(String chatroomId);

    @Query("FROM Chat m WHERE ( m.sender = :senderId AND m.reciever = :recipientId) OR ( m.sender = :recipientId AND m.reciever = :senderId)  ORDER BY m.created DESC")
    List<Chat> findChatMessagesFromSelectedUser(Long senderId, Long recipientId);

    @Query("SELECT c FROM Chat c WHERE c.chatroomId = :chatroomId ORDER BY c.created DESC")
    List<Chat>  findByChatroomIdOrderByCreatedDesc( String chatroomId);


    List<Chat> findChatsByRecieverAndIsReadIsFalse(Long receiver);
}
