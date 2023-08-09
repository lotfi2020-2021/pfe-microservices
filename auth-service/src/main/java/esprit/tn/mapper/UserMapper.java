package esprit.tn.mapper;


import esprit.tn.dto.UserDto;
import esprit.tn.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping( source = "id",target = "id")
    @Mapping(source = "email",target = "email")
    @Mapping( source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping( source = "intro",target = "intro")
    @Mapping(source = "gender",target = "gender")
    @Mapping( source = "hometown",target = "hometown")
    @Mapping(source = "currentCity",target = "currentCity")

    @Mapping(source = "eduInstitution",target = "eduInstitution")
    @Mapping( source = "workplace",target = "workplace")
    @Mapping(source = "profilePhoto",target = "profilePhoto")

    @Mapping(source = "telephone",target = "telephone")
    @Mapping( source = "enabled",target = "enabled")
    @Mapping(source = "accountVerified",target = "accountVerified")
    @Mapping(source = "emailVerified",target = "emailVerified")
    @Mapping( source = "birthDate",target = "birthDate")

    @Mapping(source = "role",target = "role")
    @Mapping(source = "country",target = "country")
    @Mapping(source = "entreprise.id",target = "entrepriseId")
    @Mapping(source = "group.id",target = "groupId")
    UserDto userToUserDTO(User user);

    List<UserDto> usersToUsersDTO(List<User> users);
}
