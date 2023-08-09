package esprit.tn.repository;

import esprit.tn.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {
    @Query("FROM Chatroom c WHERE c.sender = :senderId AND c.reciever = :receiverId ORDER BY c.created DESC")
   Chatroom findChatroomBySenderIdAndRecipientId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


    @Query("SELECT  c.chatroomId FROM Chatroom c WHERE c.sender = :user")
    List<String> findAllDistinctBySender(@Param("user") Long user);


}
