package com.openhack.repository;

import com.openhack.domain.Configuration;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationRepository implements PanacheRepositoryBase<Configuration, Long> {

}
