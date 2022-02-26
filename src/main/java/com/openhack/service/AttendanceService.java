package com.openhack.service;

import com.openhack.service.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceService {
    AttendanceDTO save(AttendanceDTO attendanceDTO);
    List<AttendanceDTO> findByIdentifier(Long id);
}
