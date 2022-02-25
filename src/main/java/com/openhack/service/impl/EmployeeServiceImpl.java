package com.openhack.service.impl;

import com.openhack.domain.Embeddings;
import com.openhack.domain.Employee;
import com.openhack.exceptions.CustomException;
import com.openhack.repository.EmbeddingsRepository;
import com.openhack.repository.EmployeeRepository;
import com.openhack.rest.client.EmbeddingsService;
import com.openhack.service.EmployeeService;
import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.dto.EmbeddingDTO;
import com.openhack.service.dto.EmployeeDTO;
import com.openhack.service.dto.request.EmbeddingRequestDTO;
import com.openhack.service.AttendanceService;
import com.openhack.service.mapper.EmbeddingMapper;
import com.openhack.service.mapper.EmployeeMapper;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final Logger LOG;
    private final JsonWebToken jsonWebToken;
    private final EmbeddingsService embeddingsService;
    private final EmbeddingsRepository embeddingsRepository;
    private final EmbeddingMapper embeddingsMapper;
    private final AttendanceService attendanceService;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, Logger log, JsonWebToken jsonWebToken, @RestClient EmbeddingsService embeddingsService, EmbeddingsRepository embeddingsRepository, EmbeddingMapper embeddingsMapper, AttendanceService attendanceService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        LOG = log;
        this.jsonWebToken = jsonWebToken;
        this.embeddingsService = embeddingsService;
        this.embeddingsRepository = embeddingsRepository;
        this.embeddingsMapper = embeddingsMapper;
        this.attendanceService = attendanceService;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findByIdOptional(id).map(employeeMapper::toDto);
    }

    @Override
    @Transactional
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        LOG.info("Employee Details {}", employeeDTO);
        employeeDTO.setIdentifier(jsonWebToken.getClaim("preferred_username"));
        Employee employee = employeeMapper.toEntity(employeeDTO);
        LOG.info("employee {}", employee.getName());

        employeeRepository.persist(employee);
        return employeeMapper.toDto(employee);
    }

    @Override
    public List<Double> retriveEmbedding(MultipartFormDataInput formDataInput) throws IOException {
        Map<String, List<InputPart>> uploadForm = formDataInput.getFormDataMap();
        List<String> fileNames = new ArrayList<>();
        LOG.info("{} ",String.join(" " ,uploadForm.keySet()));
        List<InputPart> inputParts = uploadForm.get("file");
        List<Double> embeddings = null;
        for (InputPart inputPart : inputParts) {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                LOG.info("file Name {}", fileName);
                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String encoded = Base64.getEncoder().encodeToString(bytes);
                EmbeddingRequestDTO embeddingRequestDTO = new EmbeddingRequestDTO();
                embeddingRequestDTO.setImage(encoded);
                embeddings = embeddingsService.getEmbeddings(embeddingRequestDTO);
        }
        if(embeddings == null){
            throw new CustomException("Face Not detected");
        }
        String username  = jsonWebToken.getClaim("preferred_username");
        Optional<Employee> employeeOptional = employeeRepository.findByIdentifierOptional(username);
        Employee employee;
        if(employeeOptional.isEmpty()){
            employee = new Employee();
            employee.setIdentifier(username);
            employee.setEncryptionKey("");
            employee.setName(jsonWebToken.getName());
            employeeRepository.persist(employee);
        } else {
            employee = employeeOptional.get();
        }

        EmbeddingDTO embeddingDTO= new EmbeddingDTO();
        embeddingDTO.setEmbedding(embeddings);
        embeddingDTO.setEmployee(employee);
        Embeddings embeddings1 = embeddingsMapper.toEntity(embeddingDTO);
        embeddingsRepository.persist(embeddings1);
        LOG.info("{} ", embeddingsMapper.toDto(embeddings1));
        return embeddings;
    }

    @Override
    public AttendanceDTO authenticate(MultipartFormDataInput formDataInput, String macAddress) throws IOException {
        List<Double> embeddings = processMultiPart(formDataInput);
        Boolean authenticated = false;
        Boolean faceDetected = false;
        String identifier = "";
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setAuthenticated(authenticated);
        attendanceDTO.setEmbeddings(embeddings);
        attendanceDTO.setFaceDetected(faceDetected);
        attendanceDTO.setCreatedAt(Instant.now());
        attendanceDTO.setIdentifier(identifier);
        attendanceDTO.setMacAddress(macAddress);
        return attendanceService.save(attendanceDTO);
    }


    private List<Double> processMultiPart(MultipartFormDataInput formDataInput) throws IOException {
        Map<String, List<InputPart>> uploadForm = formDataInput.getFormDataMap();
        List<String> fileNames = new ArrayList<>();
        LOG.info("{} ",String.join(" " ,uploadForm.keySet()));
        List<InputPart> inputParts = uploadForm.get("file");
        List<Double> embeddings = null;
        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            String fileName = getFileName(header);
            LOG.info("file Name {}", fileName);
            //convert the uploaded file to inputstream
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String encoded = Base64.getEncoder().encodeToString(bytes);
            EmbeddingRequestDTO embeddingRequestDTO = new EmbeddingRequestDTO();
            embeddingRequestDTO.setImage(encoded);
            embeddings = embeddingsService.getEmbeddings(embeddingRequestDTO);
        }
        if(embeddings == null || embeddings.isEmpty()){
            throw new CustomException("Face Not detected");
        }
        return embeddings;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }
}
