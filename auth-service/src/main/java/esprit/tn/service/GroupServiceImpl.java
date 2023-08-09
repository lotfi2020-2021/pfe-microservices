package esprit.tn.service;

import esprit.tn.dto.GroupDto;
import esprit.tn.entity.Department;
import esprit.tn.entity.Group;
import esprit.tn.mapper.GroupMapper;
import esprit.tn.repository.DepartmentRepository;
import esprit.tn.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {



    @Autowired
    GroupRepository groupRepository ;
    @Autowired
    DepartmentRepository departmentRepository ;
    @Autowired
    GroupMapper groupMapper ;
    @Override
    public List<GroupDto> getGroups() {
        List<Group> groups = groupRepository.findAll();

        return groupMapper.groupsTogroupsDTO(groups);
    }

    @Override
    public GroupDto getGroupById(Long idGroup) {
        Group group = groupRepository.findById(idGroup).get();
        GroupDto groupDto = groupMapper.GroupToGroupDTO( group) ;

        return groupDto;
    }

    @Override
    public GroupDto createGroup(Group group , Long idDepartment) {

        Department department = departmentRepository.findById(idDepartment).get() ;
        Optional<Group> existingGroup = groupRepository.findByName(group.getName());

        if(existingGroup.isPresent()) {
            throw new IllegalArgumentException("A group with this name already exists");
        } else {
            group.setDepartment(department);
            group.setCreatedAt(new Date());
            Group newGroup = groupRepository.save(group);

            return groupMapper.GroupToGroupDTO(newGroup);
        }
    }


    @Override
    public GroupDto updateGroup(Long idGroup, Group updatedGroup) {
        Optional<Group> existingGroup = groupRepository.findById(idGroup);

        if (existingGroup.isPresent()) {
            Group group = existingGroup.get();

            if (!group.getName().equals(updatedGroup.getName())) {
                Optional<Group> existingGroupWithName = groupRepository.findByName(updatedGroup.getName());

                if (existingGroupWithName.isPresent()) {
                    throw new IllegalArgumentException("A group with this name already exists");
                }
            }
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
            group.setResponsableGroup(updatedGroup.getResponsableGroup());
            group.setUpdatedAt(new Date());

            Group updated = groupRepository.save(group);
            return groupMapper.GroupToGroupDTO(updated);
        }

        return null;
    }


    @Override
    public void deleteGroup(Long id) {
        Optional<Group> existingGroup = groupRepository.findById(id);
        existingGroup.ifPresent(groupRepository::delete);
    }

    @Override
    public List<GroupDto> getListGroupbyDepartmentId(Long idDepartment) {
        Department department = departmentRepository.findById(idDepartment).get();
        if(department==null){
            return null ;
        }else{
            List<Group> groups = groupRepository.findAllByDepartmentId(idDepartment) ;
            List<GroupDto> groupDtos = groupMapper.groupsTogroupsDTO(groups);

            return groupDtos;
        }

    }

}
