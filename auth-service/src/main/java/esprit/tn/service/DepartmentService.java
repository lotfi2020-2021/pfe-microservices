package esprit.tn.service;

import esprit.tn.dto.DepartmentDto;
import esprit.tn.entity.Department;

import java.util.List;


public interface DepartmentService {
    List<DepartmentDto> getAllDepartments();

    DepartmentDto getDepartmentById(Long idDepartment);

    DepartmentDto createDepartment(Department department , Long idEntreprise );

    DepartmentDto updateDepartment(Long idDepartment, Department department);

    void deleteDepartment(Long id);

    List<DepartmentDto> getListDEpartmentbyEntrepriseId(Long id);
}
