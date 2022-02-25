package com.openhack.service;

import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.dto.EmployeeDTO;
import com.openhack.service.dto.request.EmbeddingRequestDTO;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface EmployeeService {
    List<EmployeeDTO> findAll();
    Optional<EmployeeDTO> findById(Long id);
    EmployeeDTO save(EmployeeDTO employeeDTO);
    List<Double> retriveEmbedding(MultipartFormDataInput embeddingRequestDTO) throws IOException;
    AttendanceDTO authenticate(MultipartFormDataInput formDataInput, String macAddress) throws IOException;
}
