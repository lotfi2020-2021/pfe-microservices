package esprit.tn.mapper;

import esprit.tn.entity.*;
import esprit.tn.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapstructMapperUpdate {
    @Mapping(target = "id")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "entreprise", ignore = true)
    void updateUserFromUserUpdateDto(UpdateUserInfoDto updateUserInfoDto, @MappingTarget User user);
}
