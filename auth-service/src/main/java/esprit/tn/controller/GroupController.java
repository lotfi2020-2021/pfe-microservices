package esprit.tn.controller;

import esprit.tn.dto.GroupDto;
import esprit.tn.entity.Group;
import esprit.tn.service.GroupService;
import esprit.tn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups (){
        List<GroupDto> groups = groupService.getGroups();

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        GroupDto group = groupService.getGroupById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(group);
    }

    @PostMapping("/Department/{DepartmentId}")
    public ResponseEntity<GroupDto> createGroup(@RequestBody Group group , @PathVariable("DepartmentId") Long departmentId) {

        GroupDto newGroup = groupService.createGroup(group ,departmentId);

        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable("id") Long idDepartment, @RequestBody Group group) {
        GroupDto updatedGroup = groupService.updateGroup(idDepartment, group);
        if (updatedGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/Department/{id}")
    public ResponseEntity<?> getListGroupbyDepartmentId(@PathVariable Long id) {
        List <GroupDto> departments = groupService.getListGroupbyDepartmentId(id) ;
        if (departments .isEmpty()) {
            return new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(departments);
    }


    @PostMapping("/{groupId}/UserGroup")
    public ResponseEntity<String> assignUserToGroup(  @RequestParam("email") String email ,@PathVariable("groupId") Long groupId) {
        userService.assignUserToGroupByEmail(email, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }





    @DeleteMapping("/UserGroup/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable("userId") Long userId) {
        userService.removeUserFromGroup(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
