package esprit.tn.mapper;

import esprit.tn.dto.DepartmentDto;

import esprit.tn.entity.Department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring" )
public interface DepartmentMapper {

    @Mapping( source = "id",target = "id")
    @Mapping(source = "email",target = "email")
    @Mapping( source = "name",target = "name")
    @Mapping( source = "responsableDepartment",target = "responsableDepartment")
    @Mapping( source = "telephone",target = "telephone")
    @Mapping( source = "entreprise.id",target = "entrepiseId")
    DepartmentDto departmentToDepartmentDTO(Department department);

    List<DepartmentDto> departmentsToDepartmentsDTO(List<Department> departments);
}
