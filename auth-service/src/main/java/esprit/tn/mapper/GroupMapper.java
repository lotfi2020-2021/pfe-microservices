package esprit.tn.mapper;


import esprit.tn.dto.GroupDto;

import esprit.tn.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = { DepartmentMapper.class, UserMapper.class})
public interface GroupMapper {


    @Mapping( source = "id",target = "id")
    @Mapping(source = "description",target = "description")
    @Mapping( source = "name",target = "name")
    @Mapping( source = "responsableGroup",target = "responsableGroup")
    @Mapping( source = "department",target = "department")
    @Mapping( source = "employees",target = "employees")
    GroupDto GroupToGroupDTO(Group group);

    List<GroupDto> groupsTogroupsDTO(List<Group> groups);
}
