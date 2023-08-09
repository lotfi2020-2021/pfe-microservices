package esprit.tn.service;

import  esprit.tn.dto.*;
import esprit.tn.entity.User;
import esprit.tn.enumeration.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);
    User getUserByEmail(String email);
  
    User createNewUser(SignupDto signupDto);
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto ,Long id );
    User updateEmail(UpdateEmailDto updateEmailDto ,Long id);
    User updatePassword(UpdatePasswordDto updatePasswordDto  , Long id );


    User verifyEmail(String token);
    void forgotPassword(String email);
    User resetPassword(String token, ResetPasswordDto resetPasswordDto);

    void deleteUserbyId(Long id);

    User getAuthenticatedUser();

    List<User> getListEntreprise(Role name);

//	User createNewModerateur(User signupDto);
	User createEntrerise(User signupDto);

	User updateRoleToUser(Long iduser);
	User updateRoleToAdmin(Long iduser);
	User updateRoleToModerateur(Long iduser);
	User bloquerOrDebloquerUser(Long iduser);
    List<User> getListusersByEntreprise(Long id);

    User updatePhoto(User user, Long id) ;
    List<UserDto> listUsersByGrouôId(Long id) ;

     List<UserDto> findUsersWithoutGroupId() ;

    void assignUserToGroupByEmail(String email, Long groupId) ;

     void removeUserFromGroup(Long userId);

   // List<User> getUsersWithoutChatroomByEntrepriseId(Long entrepriseId, Long userId);
}
