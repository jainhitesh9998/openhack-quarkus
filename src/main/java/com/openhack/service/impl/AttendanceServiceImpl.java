package com.openhack.service.impl;

import com.openhack.domain.Attendance;
import com.openhack.repository.AttendanceRepository;
import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.mapper.AttendanceMapper;
import com.openhack.service.AttendanceService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final Logger LOG;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(Logger log, AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper) {
        LOG = log;
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceDTO save(AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendanceRepository.persist(attendance);
        return attendanceMapper.toDto(attendance);
    }
}
