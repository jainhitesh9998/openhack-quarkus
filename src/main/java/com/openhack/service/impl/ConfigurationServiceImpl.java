package com.openhack.service.impl;

import com.openhack.domain.Configuration;
import com.openhack.domain.Employee;
import com.openhack.exceptions.CustomException;
import com.openhack.repository.ConfigurationRepository;
import com.openhack.service.AttendanceService;
import com.openhack.service.ConfigurationService;
import com.openhack.service.EmployeeService;
import com.openhack.service.dto.AnalyticsDTO;
import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.dto.ConfigurationDTO;
import com.openhack.service.dto.EmployeeDTO;
import com.openhack.service.dto.request.ConfigurationChangeDTO;
import com.openhack.service.mapper.ConfigurationMapper;
import com.openhack.service.messaging.ConfigurationProducer;
import io.quarkus.panache.common.Sort;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {
    private final Logger LOG;
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;
    private final ConfigurationProducer configurationProducer;
    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public ConfigurationServiceImpl(Logger log, ConfigurationRepository configurationRepository, ConfigurationMapper configurationMapper, ConfigurationProducer configurationProducer, AttendanceService attendanceService, EmployeeService employeeService) {
        LOG = log;
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
        this.configurationProducer = configurationProducer;
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
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
        configurationDTO.setCreatedAt(null);
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
        return configurationRepository.listAll(Sort.by("updated_at").descending()).stream().map(configurationMapper::toDto)
                .peek(configurationDTO -> {
                    if(configurationDTO.getUpdatedAt().isBefore(Instant.now().minusSeconds(60))){
                        configurationDTO.setStatus(false);
                    }
                }).collect(Collectors.toList());
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

    @Override
    public AnalyticsDTO getAnalytics() {
        List<ConfigurationDTO> all = getAll();
        List<AttendanceDTO> attendanceDTOS = attendanceService.getAll();
        List<EmployeeDTO> employeeDTOS = employeeService.findAll();
        AnalyticsDTO analyticsDTO = new AnalyticsDTO();

        analyticsDTO.setTotalDevices((long) all.size());
        analyticsDTO.setActiveDevices(all.stream().filter(
                ConfigurationDTO::getStatus
        ).count());
        analyticsDTO.setInactiveDevices(analyticsDTO.getTotalDevices() - analyticsDTO.getActiveDevices());
        analyticsDTO.setDisabledDevices(all.stream().filter(
                configurationDTO -> !configurationDTO.getEnabled()
        ).count());

        analyticsDTO.setTotalAuth((long) attendanceDTOS.size());
        analyticsDTO.setTotalAuthInDay((long) (int) attendanceDTOS.stream().filter(
                attendanceDTO -> attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400))
        ).count());
        Set<Long> set = new HashSet<>();
        analyticsDTO.setUniqueAuthInDay(attendanceDTOS.stream().filter(
                attendanceDTO -> attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400))
        ).filter(
                attendanceDTO -> set.add(attendanceDTO.getIdentifier())).count()
        );

        return analyticsDTO;

    }
}
