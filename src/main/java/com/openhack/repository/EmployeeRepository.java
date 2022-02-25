package com.openhack.repository;

import com.openhack.domain.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepositoryBase<Employee, Long> {
    public Optional<Employee> findByIdentifierOptional(String identifier){
        return find("identifier", identifier).stream().findAny();
    }
}
