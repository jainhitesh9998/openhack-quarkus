package com.openhack.service.impl;

import com.openhack.domain.Configuration;
import com.openhack.exceptions.CustomException;
import com.openhack.repository.ConfigurationRepository;
import com.openhack.service.ConfigurationService;
import com.openhack.service.dto.ConfigurationDTO;
import com.openhack.service.dto.request.ConfigurationChangeDTO;
import com.openhack.service.mapper.ConfigurationMapper;
import com.openhack.service.messaging.ConfigurationProducer;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {
    private final Logger LOG;
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;
    private final ConfigurationProducer configurationProducer;

    public ConfigurationServiceImpl(Logger log, ConfigurationRepository configurationRepository, ConfigurationMapper configurationMapper, ConfigurationProducer configurationProducer) {
        LOG = log;
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
        this.configurationProducer = configurationProducer;
    }

    @Override
    public ConfigurationDTO save(ConfigurationDTO configurationDTO) {
        configurationDTO.setCreatedAt(Instant.now());
        configurationDTO.setUpdatedAt(Instant.now());
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
        ConfigurationChangeDTO configurationChangeDTO = new ConfigurationChangeDTO();
        configurationChangeDTO.setDeviceIdentifier(configurationDTO.getDeviceIdentifier());
        configurationChangeDTO.setEnabled(configurationDTO.getEnabled());
        configurationProducer.send(configurationChangeDTO);
//        configurationProducer.publish(configurationChangeDTO);
//        LOG.info("{} message sent", configurationChangeDTOMulti.emitOn());
        configurationDTO.setUpdatedAt(Instant.now());
        configurationMapper.partialUpdate(configurationDb.get(), configurationDTO);
        configurationRepository.persist(configurationDb.get());
        LOG.info("updated entity {}", configurationMapper.toDto(configurationDb.get()));
        return configurationMapper.toDto(configurationDb.get());
    }

    @Override
    public List<ConfigurationDTO> getAll() {
//        configurationRepository.
        return configurationRepository.listAll(Sort.by("updated_at").descending()).stream().map(configurationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ConfigurationDTO> getConfigByIdentifier(String identifier) {
        return configurationRepository.findByIdentifierOptional(identifier).map(configurationMapper::toDto);
    }

    @Transactional
    @Override
    public void updateConfigStatus(String deviceIdentifier, Boolean status) {
        Optional<ConfigurationDTO> optionalConfigurationDTO  = getConfigByIdentifier(deviceIdentifier);
        if(optionalConfigurationDTO.isEmpty()){
            LOG.info("device Not Found");
        } else {
            ConfigurationDTO configurationDTO = optionalConfigurationDTO.get();
            configurationDTO.setStatus(status);
            update(configurationDTO);
        }
    }
}
