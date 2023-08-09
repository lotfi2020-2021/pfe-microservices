package esprit.tn.mapper;


import esprit.tn.entity.*;
import esprit.tn.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MapStructMapper {
    User userUpdateDtoToUser(UpdateUserInfoDto updateUserInfoDto);


}
