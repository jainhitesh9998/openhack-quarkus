package com.openhack.repository;

import com.openhack.domain.Attendance;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepositoryBase<Attendance, Long> {
    public List<Attendance> findByIdentifier(Long id){
        return list("identifier", id);
    }
}
