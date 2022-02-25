package com.openhack.repository;

import com.openhack.domain.Attendance;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepositoryBase<Attendance, Long> {
}
