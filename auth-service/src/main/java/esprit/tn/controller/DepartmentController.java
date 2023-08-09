package esprit.tn.controller;


import esprit.tn.dto.DepartmentDto;
import esprit.tn.entity.Department;
import esprit.tn.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartments();

        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(department);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody Department department , @PathVariable("userId") Long idUser) {

        DepartmentDto newDepartment = departmentService.createDepartment(department ,idUser);

        return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long idDepartment, @RequestBody Department department) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(idDepartment, department);
        if (updatedDepartment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/User/{id}")
    public ResponseEntity<?> getListDepartmentbyEntrepriseId(@PathVariable Long id) {
        List <DepartmentDto> departments = departmentService.getListDEpartmentbyEntrepriseId(id) ;
        if (departments .isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(departments,HttpStatus.OK);

    }


}
