package com.openhack.service.impl;

import com.openhack.domain.Attendance;
import com.openhack.repository.AttendanceRepository;
import com.openhack.service.ConfigurationService;
import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.dto.ConfigurationDTO;
import com.openhack.service.mapper.AttendanceMapper;
import com.openhack.service.AttendanceService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final Logger LOG;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final ConfigurationService configurationService;

    public AttendanceServiceImpl(Logger log, AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper, ConfigurationService configurationService) {
        LOG = log;
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.configurationService = configurationService;
    }

    @Override
    public AttendanceDTO save(AttendanceDTO attendanceDTO) {
        Optional<ConfigurationDTO> configByIdentifier = configurationService.getConfigByIdentifier(attendanceDTO.getDeviceIdentifier());
        configByIdentifier.ifPresent(configurationDTO -> attendanceDTO.setLocation(configurationDTO.getLocation()));
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendanceRepository.persist(attendance);
        return attendanceMapper.toDto(attendance);
    }

    @Override
    public List<AttendanceDTO> findByIdentifier(Long id) {
        return attendanceRepository.findByIdentifier(id).stream().map(attendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAll() {
        return attendanceRepository.findAll().stream().map(attendanceMapper::toDto).collect(Collectors.toList());
    }

}
