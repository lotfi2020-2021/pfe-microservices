package esprit.tn.service;


import esprit.tn.dto.GroupDto;
import esprit.tn.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> getGroups();

    GroupDto getGroupById(Long idGroup);

    GroupDto createGroup(Group group , Long idDepartment );

    GroupDto updateGroup(Long idGroup, Group group);

    void deleteGroup(Long id);

    List<GroupDto> getListGroupbyDepartmentId(Long idDepartment);
}
