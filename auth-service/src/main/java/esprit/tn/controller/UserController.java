package esprit.tn.controller;

import esprit.tn.common.AppConstants;
import esprit.tn.common.UserPrincipal;
import esprit.tn.dto.*;
import esprit.tn.entity.User;
import esprit.tn.enumeration.Role;
import esprit.tn.mapper.UserMapper;
import esprit.tn.repository.UserRepository;
import esprit.tn.service.EmailService;
import esprit.tn.service.IFileService;
import esprit.tn.service.JwtTokenService;
import esprit.tn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private  UserService userService;


    private final UserMapper userMapper;

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    @Autowired
    private IFileService serviceFile ;
    @Autowired
    private EmailService emailService;

    @Autowired
    ServletContext context;
    String subPath = "photos";

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupDto signupDto) {
        User savedUser = userService.createNewUser(signupDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    
    @PostMapping("/CreateEntreprise")
    public ResponseEntity<?> CreateModerateur(@RequestBody @Valid User signupDto) {
       User savedUser = userService.createEntrerise(signupDto);
      return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
   }
//
//    @PostMapping("/CreateAdmin")
//    public ResponseEntity<?> CreateAdmin(@RequestBody @Valid User signupDto) {
//        User savedUser = userService.createNewAdmin(signupDto);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//    }
//
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword())
        );
        User loginUser = userRepository.findByEmail(loginDto.getEmail()).get();
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
         UserDto user = userMapper.userToUserDTO(loginUser) ;

        HttpHeaders newHttpHeaders = new HttpHeaders();
        newHttpHeaders.add(AppConstants.TOKEN_HEADER, jwtTokenService.generateToken(userPrincipal));
        return new ResponseEntity<>(user, newHttpHeaders, HttpStatus.OK);
    }

    @GetMapping("/Users")
    public List<UserDto> getUsers() {

    	List<User> users = userRepository.findAll();
        return   userMapper.usersToUsersDTO(users) ;
    }

    @GetMapping("/Entreprises")
    public List<UserDto> getEntreprises() {
            Role role = Role.entreprise;
        List<User> entreprises = userService.getListEntreprise(role);
        return   userMapper.usersToUsersDTO(entreprises) ;
    }

    @GetMapping("/Entreprise/users/{id}")
    public List<UserDto> getUsersByEntrpriseId(@PathVariable Long id) {
        List<User> users = userService.getListusersByEntreprise( id );
        return   userMapper.usersToUsersDTO(users) ;
    }


//    @GetMapping("/listUserWithoutChatroom/{userId}/{entrepriseId}")
//    public ResponseEntity<List<UserDto>> getUsersWithoutChatroomByEntrepriseId(@PathVariable Long entrepriseId ,@PathVariable Long userId ) {
//        List<User> users = userService.getUsersWithoutChatroomByEntrepriseId(entrepriseId,userId);
//        List<UserDto> usersDto=   userMapper.usersToUsersDTO(users) ;
//        return ResponseEntity.ok(usersDto);
//    }

    @PutMapping("/Role/User/{userId}")
    public ResponseEntity<?> updateRoleToUser(@PathVariable("userId") Long userId) {
        User authUser = userService.updateRoleToUser(userId);
       
                
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @PutMapping("/Role/Admin/{userId}")
    public ResponseEntity<?> updateRoleToAdmin(@PathVariable("userId") Long userId) {
        User authUser = userService.updateRoleToAdmin(userId);
       
                
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }
    @PutMapping("/Role/Moderateur/{userId}")
    public ResponseEntity<?> updateRoleToModerateur(@PathVariable("userId") Long userId) {
        User authUser = userService.updateRoleToModerateur(userId);
       
                
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @PutMapping("/User/Bloquer/{userId}")
    public ResponseEntity<?> bloquerOrDebloquerUser(@PathVariable("userId") Long userId) {
        User authUser = userService.bloquerOrDebloquerUser(userId);
       
                
        return new ResponseEntity<>(authUser, HttpStatus.OK);
    }

    @GetMapping("/User/email")
    public ResponseEntity<?> getUserbyEmail( @RequestBody @Valid  String email) {
        User user = userRepository.findByEmail(email).get();
       UserDto userDto = userMapper.userToUserDTO(user) ;
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/User/emailid")
    public Long getUserbyEmail2( @RequestParam("email") @Valid String email) {
        User user = userRepository.findByEmail(email).get();

        return user.getId() ;
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> showUserProfile(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getPrincipal().toString());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping ("/account/update/info/{id}")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserInfoDto updateUserInfoDto , @PathVariable("id") Long id ) {
        User updatedUser = userService.updateUserInfo(updateUserInfoDto , id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping ("/account/update/email/{id}")
    public ResponseEntity<?> updateUserEmail(@RequestBody @Valid UpdateEmailDto updateEmailDto , @PathVariable("id") Long id ) {
        userService.updateEmail(updateEmailDto , id );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/account/update/password/{id}")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto ,  @PathVariable("id")  Long id) {
        userService.updatePassword(updatePasswordDto , id );
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PutMapping ("/account/update/profile-photo/{id}")
    public ResponseEntity<?> updateProfilePhoto( @RequestParam(name = "profilePhoto",required = false) MultipartFile fileName , @PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            ResponseEntity.badRequest().build();
        }
        String imageName = user.get().getProfilePhoto();
        if (fileName!= null) {
            if (imageName != null) {
                serviceFile.deleteFile(this.subPath + "/" + imageName);
            }

            imageName = serviceFile.saveFile(fileName, this.subPath);
        }
        User newuser = new User();
        newuser.setProfilePhoto(imageName);
        userService.updatePhoto(newuser, id);
        return new  ResponseEntity(HttpStatus.OK);
    }



    @GetMapping(path="/ImageProsuser/{id}")
    public byte[] getPhotos(@PathVariable("id") Long id) throws Exception{
        User e = userRepository.findById(id).get();
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getProfilePhoto()));
    }





    @DeleteMapping ("/User/delete/{id}")
    public ResponseEntity<?> deleteUserbyId( @PathVariable("id") Long userId) {
        userService.deleteUserbyId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }





    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        User targetUser = userService.getUserById(userId);
        UserDto userDto = userMapper.userToUserDTO(targetUser) ;

        return userDto;
    }



 

    @PostMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
        userService.verifyEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        userService.forgotPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto,
                                           @PathVariable("token") String token) {
        userService.resetPassword(token, resetPasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/Users/Group/{id}")
    public ResponseEntity<?> getListDepartmentbyEntrepriseId(@PathVariable Long id) {
        List <UserDto> users = userService.listUsersByGrouôId(id) ;
        if (users .isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/withoutGroup")
    public List<UserDto> getUsersWithoutGroup() {
        return userService.findUsersWithoutGroupId();
    }


    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmailToListUsers(
            @RequestParam("toList") List<String> toList,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content
    ) {
        emailService.sendEmaiListUsers(toList, subject, content);
        return ResponseEntity.ok("Email sent successfully");
    }




}
