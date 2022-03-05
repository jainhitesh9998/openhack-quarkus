package com.openhack.service.messaging;

import com.openhack.service.AttendanceService;
import com.openhack.service.dto.AttendanceDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class AttendanceConsumer {
    @Inject
    AttendanceService attendanceService;
    @Inject
    Logger LOG;

    @Transactional
    @Incoming("topic-attendance-iot")
    public void consume(AttendanceDTO attendanceDTO){
        LOG.info("attendance {}", attendanceDTO);
        AttendanceDTO saved = attendanceService.save(attendanceDTO);
        LOG.info("saved {} ", saved);
    }
}
