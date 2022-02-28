package com.openhack.service.impl;

import com.openhack.domain.Configuration;
import com.openhack.exceptions.CustomException;
import com.openhack.repository.ConfigurationRepository;
import com.openhack.service.ConfigurationService;
import com.openhack.service.dto.ConfigurationDTO;
import com.openhack.service.mapper.ConfigurationMapper;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {
    private final Logger LOG;
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    public ConfigurationServiceImpl(Logger log, ConfigurationRepository configurationRepository, ConfigurationMapper configurationMapper) {
        LOG = log;
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
    }

    @Override
    public ConfigurationDTO save(ConfigurationDTO configurationDTO) {
        Configuration configuration = configurationMapper.toEntity(configurationDTO);
        configurationRepository.persist(configuration);
        return configurationMapper.toDto(configuration);
    }

    @Override
    public ConfigurationDTO update(ConfigurationDTO configurationDTO) {
        if(configurationDTO.getId() == null){
            throw  new CustomException("Id not present for update");
        }
        Optional<Configuration> configurationDb = configurationRepository.findByIdOptional(configurationDTO.getId());
        if(configurationDb.isEmpty()){
            throw new CustomException("Data Not Present In DB");
        }
        configurationMapper.partialUpdate(configurationDb.get(), configurationDTO);
        configurationRepository.persist(configurationDb.get());
        LOG.info("updated entity {}", configurationMapper.toDto(configurationDb.get()));
        return configurationMapper.toDto(configurationDb.get());
    }

    @Override
    public List<ConfigurationDTO> getAll() {
        return configurationRepository.findAll().stream().map(configurationMapper::toDto).collect(Collectors.toList());
    }
}
