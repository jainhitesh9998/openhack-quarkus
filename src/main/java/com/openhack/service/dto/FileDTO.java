package com.openhack.service.dto;

import com.openhack.domain.Employee;
import com.openhack.domain.enumeration.EncryptionAlgorithm;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;

@RegisterForReflection
@Data
public class FileDTO implements Serializable {
    private Long id;
    private String identifier;
    private String uri;
    private Boolean encryption;
    private EncryptionAlgorithm encryptionAlgorithm;
    private Employee employee;
}
