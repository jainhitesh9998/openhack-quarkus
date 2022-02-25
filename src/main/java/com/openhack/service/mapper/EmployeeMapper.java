package com.openhack.service.mapper;

import com.openhack.domain.Employee;
import com.openhack.service.dto.EmployeeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "cdi", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

//    EmployeeDTO toDomain(Employee employee);
//
////    @Mapping(target = "embeddings", ignore = true)
////    @Mapping(target = "files", ignore = true)
//    Employee toEntity(EmployeeDTO employeeDTO);
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoId(Employee employee);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Employee toEntityId(EmployeeDTO employee);
}
