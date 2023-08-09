package esprit.tn.service;

import  esprit.tn.common.AppConstants;
import  esprit.tn.common.UserPrincipal;
import esprit.tn.entity.Country;
import esprit.tn.entity.Group;
import esprit.tn.entity.User;
import esprit.tn.enumeration.Role;
import esprit.tn.exception.EmailExistsException;
import esprit.tn.exception.InvalidOperationException;
import esprit.tn.exception.SameEmailUpdateException;
import esprit.tn.exception.UserNotFoundException;
import esprit.tn.mapper.MapstructMapperUpdate;
import esprit.tn.mapper.UserMapper;
import esprit.tn.repository.GroupRepository;
import esprit.tn.repository.UserRepository;

import esprit.tn.dto.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository ;

    @Autowired
    private  CountryService countryService;

    private final UserMapper userMapper;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    private final MapstructMapperUpdate mapstructMapperUpdate;
    private final Environment environment;




	@Override
	    public User bloquerOrDebloquerUser(Long iduser) {
	        User authUser =  userRepository.findUserById(iduser);

	        if(authUser.getEnabled()==true) {
	        authUser.setEnabled(false);
	        }
	        else {

	        	authUser.setEnabled(true);
	        }
	        return userRepository.save(authUser);
	    }

    @Override
    public List<User> getListusersByEntreprise(Long id) {
        List <User> users = userRepository.findByEntrepriseId(id) ;
     return   users ;
    }

    @Override
    public List<UserDto> listUsersByGrouôId(Long id) {
        Group group = groupRepository.findById(id).get();
        if (group == null) {
            return null;
        } else {
            List<User> users = userRepository.findusersByGroupId(id);
            List<UserDto> usersDtos = userMapper.usersToUsersDTO(users);

            return usersDtos;
        }

    }

    @Override
    public List<UserDto> findUsersWithoutGroupId() {
        List<User> list = userRepository.findUsersWithoutGroupId();
        return  userMapper.usersToUsersDTO(list) ;
    }

    @Override
    public void assignUserToGroupByEmail(String email, Long groupId) {
        try {
            Optional<User> list =  userRepository.findByEmail(email);

            User user =list.get() ;

            if (user.getGroup() != null) {
                throw new IllegalArgumentException("User already belongs to a group");
            }
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found with id " + groupId));
            user.setGroup(group);
            userRepository.save(user);
        } catch (NotFoundException e) {
            // handle the exception as appropriate
        }

    }


    @Override
    public void removeUserFromGroup(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id " + userId));
            user.setGroup(null);
            userRepository.save(user);
        } catch (javassist.NotFoundException e) {
            // handle the exception as appropriate
        }
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> getListEntreprise(Role name) {
        return userRepository.findByRole(name);
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }


  

    @Override
    public User createNewUser(SignupDto signupDto) {
        try {
            User user = getUserByEmail(signupDto.getEmail());
            if (user != null) {
                throw new EmailExistsException();
            }
        } catch (UserNotFoundException e) {
            User newUser = new User();
            newUser.setEmail(signupDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            newUser.setFirstName(signupDto.getFirstName());
            newUser.setLastName(signupDto.getLastName());
       
            newUser.setEnabled(true);
            newUser.setAccountVerified(false);
            newUser.setEmailVerified(false);
            newUser.setJoinDate(new Date());
            newUser.setDateLastModified(new Date());
            newUser.setRole(Role.user);
            User savedUser = userRepository.save(newUser);
            UserPrincipal userPrincipal = new UserPrincipal(savedUser);
            String emailVerifyMail =
                    emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(savedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
            return savedUser;
        }
        return null;
    }




  
//	@Override
//    public User createNewAdmin(User signupDto) {
//        try {
//            User user = getUserByEmail(signupDto.getEmail());
//            if (user != null) {
//                throw new EmailExistsException();
//            }
//        } catch (UserNotFoundException e) {
//        	Random r =new Random();
//        	String combainaison= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,><%£¤$&^#!?/*-+{}@=:_()°]";
//        	int len =8;
//        	char[] password =new char[len];
//        	for (int i=0;i<len;i++) {
//
//        		password[i]=combainaison.charAt(r.nextInt(combainaison.length()));
//
//        	}
//
//            User newUser = new User();
//            newUser.setEmail(signupDto.getEmail());
//            newUser.setPassword(passwordEncoder.encode(new String(password)));
//            System.out.println("password"+ new String(password));
//            newUser.setFirstName(signupDto.getFirstName());
//            newUser.setLastName(signupDto.getLastName());
//
//            newUser.setEnabled(true);
//            newUser.setAccountVerified(false);
//            newUser.setEmailVerified(false);
//            newUser.setJoinDate(new Date());
//            newUser.setDateLastModified(new Date());
//            newUser.setRole(Role.admin.name());
//            User savedUser = userRepository.save(newUser);
//            UserPrincipal userPrincipal = new UserPrincipal(savedUser);
//            String emailVerifyMail =
//                    emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
//            emailService.send(savedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
//            return savedUser;
//        }
//        return null;
//    }

	@Override
    public User createEntrerise(User signupDto) {
        try {
            User user = getUserByEmail(signupDto.getEmail());
            if (user != null) {
                throw new EmailExistsException();
            }
        } catch (UserNotFoundException e) {
        	Random r =new Random();
        	String combainaison= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,><%£¤$&^#!?/*-+{}@=:_()°]";
        	int len =8;
        	char[] password =new char[len];
        	for (int i=0;i<len;i++) {

        		password[i]=combainaison.charAt(r.nextInt(combainaison.length()));

        	}

            User newUser = new User();
            newUser.setEmail(signupDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(new String(password)));
            System.out.println("password"+ new String(password));
            newUser.setFirstName(signupDto.getFirstName());
            newUser.setLastName(signupDto.getLastName());

            newUser.setTelephone(signupDto.getTelephone());
            newUser.setAccountVerified(true);
            newUser.setEmailVerified(false);
            newUser.setJoinDate(new Date());
            newUser.setEmailVerified(true);
            newUser.setEnabled(true);
            newUser.setDateLastModified(new Date());
            newUser.setRole(Role.entreprise);
            User savedUser = userRepository.save(newUser);
            UserPrincipal userPrincipal = new UserPrincipal(savedUser);
           String message = "votre mode passe Wind Zoom :" + password ;
            emailService.send(savedUser.getEmail() , "create entreprise" , message);
            return savedUser;
        }
        return null;
    }


	@Override
	    public User updateRoleToUser(Long iduser) {
	        User authUser =  userRepository.findUserById(iduser);
	        authUser.setRole(Role.user);

	        return userRepository.save(authUser);
	    }

	@Override
	public User updateRoleToAdmin(Long iduser) {
		   User authUser =  userRepository.findUserById(iduser);
	        authUser.setRole(Role.admin);

	        return userRepository.save(authUser);
	    }

	@Override
	public User updateRoleToModerateur(Long iduser) {
		   User authUser =  userRepository.findUserById(iduser);
	        authUser.setRole(Role.entreprise);

	        return userRepository.save(authUser);
	    }

    @Override
    public User updateUserInfo(UpdateUserInfoDto updateUserInfoDto , Long id ) {
        User user = getUserById( id) ;

        if (updateUserInfoDto.getCountryName() != null) {
            Country selectedUserCountry = countryService.getCountryByName(updateUserInfoDto.getCountryName());
            user.setCountry(selectedUserCountry);
        }

        if (updateUserInfoDto.getEntrepriseId() != null) {

            User user2 = userRepository.findById(updateUserInfoDto.getEntrepriseId()).get();
            user.setEntreprise(user2) ;
        }

        mapstructMapperUpdate.updateUserFromUserUpdateDto(updateUserInfoDto, user);
        return userRepository.save(user);
    }

    @Override
    public User updateEmail(UpdateEmailDto updateEmailDto ,Long id) {
        User authUser = userRepository.findUserById(id) ;
        String newEmail = updateEmailDto.getEmail();
        String password = updateEmailDto.getPassword();

        if (!newEmail.equalsIgnoreCase(authUser.getEmail())) {
            try {
                User duplicateUser = getUserByEmail(newEmail);
                if (duplicateUser != null) {
                    throw new EmailExistsException();
                }
            } catch (UserNotFoundException e) {
                if (passwordEncoder.matches(password, authUser.getPassword())) {
                    authUser.setEmail(newEmail);
                    authUser.setEmailVerified(false);
                    authUser.setDateLastModified(new Date());
                    User updatedUser = userRepository.save(authUser);
                    UserPrincipal userPrincipal = new UserPrincipal(updatedUser);
                    String emailVerifyMail =
                            emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
                    emailService.send(updatedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
                    return updatedUser;
                } else {
                    throw new InvalidOperationException();
                }
            }
        } else {
            throw new SameEmailUpdateException();
        }
        return null;
    }

    @Override
    public User updatePassword(UpdatePasswordDto updatePasswordDto , Long id) {
        User authUser = userRepository.findUserById(id) ;
        if (passwordEncoder.matches(updatePasswordDto.getOldPassword(), authUser.getPassword())) {
            authUser.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
            authUser.setDateLastModified(new Date());
            return userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public User verifyEmail(String token) {
        String targetEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetEmail);
        targetUser.setEmailVerified(true);
        targetUser.setAccountVerified(true);
        targetUser.setDateLastModified(new Date());
        return userRepository.save(targetUser);
    }



    @Override
    public User updatePhoto(User user, Long id)
    {
        User oldUser = userRepository.findById(id).get();
        if ((!user.getProfilePhoto().isEmpty()) )
        {
            oldUser.setProfilePhoto(user.getProfilePhoto());
        }
        return userRepository.save(oldUser);
    }

    @Override
    public void forgotPassword(String email) {
        User targetUser = getUserByEmail(email);
        if(targetUser!=null) {


            UserPrincipal userPrincipal = new UserPrincipal(targetUser);
            String emailVerifyMail =
                    emailService.buildResetPasswordMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(targetUser.getEmail(), AppConstants.RESET_PASSWORD, emailVerifyMail);

        }
        else{UserNotFoundException ignored;}
    }

    @Override
    public User resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        String targetUserEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetUserEmail);
        targetUser.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        return userRepository.save(targetUser);
    }

 

    @Override
    public void deleteUserbyId(Long id) {
        userRepository.deleteById(id);
    }


    public final User getAuthenticatedUser() {
        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return getUserByEmail(authUserEmail);
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }
//    @Override
//    public List<User> getUsersWithoutChatroomByEntrepriseId(Long entrepriseId , Long userId) {
//        return userRepository.findUsersByEntrepriseIdWithoutChatroom(entrepriseId  , userId);
//    }

	
}
