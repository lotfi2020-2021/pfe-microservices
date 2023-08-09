package esprit.tn.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

        public String body ;
        public Long senderId ;
        public Long recieverId ;
        public String photoSender ;
        public String userNameSender ;
        public String photoReceiver ;
        public String userNameReceiver;
        public String chatroomId ;
        public boolean isRead ;
        public Date created ;


}
