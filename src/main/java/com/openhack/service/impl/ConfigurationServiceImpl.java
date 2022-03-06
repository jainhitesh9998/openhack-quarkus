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
        if(configurationDTO.getEnabled() != null) {
            ConfigurationChangeDTO configurationChangeDTO = new ConfigurationChangeDTO();
            configurationChangeDTO.setDeviceIdentifier(configurationDTO.getDeviceIdentifier());
            configurationChangeDTO.setEnabled(configurationDTO.getEnabled());
            configurationProducer.send(configurationChangeDTO);
        }
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
                    if(configurationDTO.getUpdatedAt().isBefore(Instant.now().minusSeconds(600))){
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
            configurationDTO.setEnabled(null);
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
        LOG.info("attendance size {}",attendanceDTOS.size());
        analyticsDTO.setTotalDevices((long) all.size());
        analyticsDTO.setActiveDevices(all.stream().filter(
                configurationDTO1 -> configurationDTO1.getStatus()!=null && configurationDTO1.getStatus()
        ).count());
        analyticsDTO.setInactiveDevices(analyticsDTO.getTotalDevices() - analyticsDTO.getActiveDevices());
        analyticsDTO.setDisabledDevices(all.stream().filter(
                configurationDTO -> configurationDTO.getEnabled() == null || !configurationDTO.getEnabled()
        ).count());

        analyticsDTO.setTotalAuth((long) attendanceDTOS.size());
        analyticsDTO.setTotalAuthInDay((long) (int) attendanceDTOS.stream().filter(
                attendanceDTO -> attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400))
        ).count());

        analyticsDTO.setTotalAuthFailure(attendanceDTOS.stream()
                .filter(attendanceDTO ->  attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400)))
                .filter(attendanceDTO -> attendanceDTO.getAuthenticated() == null || !attendanceDTO.getAuthenticated())
        .count());

        analyticsDTO.setHighTemperatureAlerts(attendanceDTOS.stream().filter(
                attendanceDTO ->attendanceDTO.getTemperature() != null && attendanceDTO.getTemperature() > 99.0 && attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400))
        ).count());
        Set<Long> set = new HashSet<>();
        analyticsDTO.setUniqueAuthInDay(attendanceDTOS.stream().filter(
                attendanceDTO -> attendanceDTO.getCreatedAt().isAfter(Instant.now().minusSeconds(86400))
        ).filter(
                attendanceDTO -> set.add(attendanceDTO.getIdentifier())).count()
        );

        analyticsDTO.setActiveUsers(employeeDTOS.stream().filter(
                employeeDTO -> employeeDTO.getEnabled()!= null && employeeDTO.getEnabled()).count()
        );
        analyticsDTO.setDisabledUsers((long) employeeDTOS.size() - analyticsDTO.getActiveUsers());


        return analyticsDTO;

    }

    @Override
    public void delete(Long id) {
        boolean b = configurationRepository.deleteById(id);
        LOG.info("Deleted {}", b);
    }
}
