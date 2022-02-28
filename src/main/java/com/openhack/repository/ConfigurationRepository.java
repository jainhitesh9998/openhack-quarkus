package com.openhack.repository;

import com.openhack.domain.Configuration;
import com.openhack.domain.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ConfigurationRepository implements PanacheRepositoryBase<Configuration, Long> {
    public Optional<Configuration> findByIdentifierOptional(String identifier){
        return find("device_identifier", identifier).stream().findAny();
    }
}
