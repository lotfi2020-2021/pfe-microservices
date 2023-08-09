package esprit.tn.service;

import esprit.tn.dto.DepartmentDto;
import esprit.tn.entity.Department;
import esprit.tn.entity.User;
import esprit.tn.mapper.DepartmentMapper;
import esprit.tn.repository.DepartmentRepository;
import esprit.tn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    DepartmentRepository departmentRepository ;
    @Autowired
    UserRepository userRepository ;
    @Autowired
    DepartmentMapper departmentMapper ;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return  departmentMapper.departmentsToDepartmentsDTO(departments);
    }

    @Override
    public DepartmentDto getDepartmentById(Long idDepartment) {
        Department department = departmentRepository.findById(idDepartment).get();
        return departmentMapper.departmentToDepartmentDTO(department);
    }

    @Override
    public DepartmentDto createDepartment(Department department, Long idEntreprise) {
        User user = userRepository.findUserById(idEntreprise) ;
        department.setEmail(department.getEmail());
        department.setName(department.getName());
        department.setEntreprise(user);
        department.setResponsableDepartment(department.getResponsableDepartment());
        department.setCreatedAt(new Date());
        Department department2 = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentDTO(department2) ;
    }

    @Override
    public DepartmentDto updateDepartment(Long idDepartment, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(idDepartment);
        Department  lastDepartment = existingDepartment.get();
        if (existingDepartment.isPresent()) {
            lastDepartment.setName(department.getName());
            lastDepartment.setEmail(department.getEmail());
            lastDepartment.setDescription(department.getDescription());
            lastDepartment.setTelephone(department.getTelephone());
            lastDepartment.setResponsableDepartment(department.getResponsableDepartment());
            lastDepartment.setUpdatedAt(new Date());
            Department department2 = departmentRepository.save(lastDepartment);



            return   departmentMapper.departmentToDepartmentDTO(department2) ;
        }
        return null;
    }

    @Override
    public void deleteDepartment(Long id) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        existingDepartment.ifPresent(departmentRepository::delete);
    }

    @Override
    public List<DepartmentDto> getListDEpartmentbyEntrepriseId(Long idUser) {
        User user = userRepository.findById(idUser).get();
        if(user==null){
            return null ;
        }else{
            List<Department> departments = departmentRepository.findByEntrepriseId(idUser) ;
            List<DepartmentDto>departmentDtos=departmentMapper.departmentsToDepartmentsDTO(departments) ;

            return departmentDtos;
        }

    }

}
