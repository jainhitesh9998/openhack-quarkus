package com.openhack.service.dto;

import com.openhack.domain.Embeddings;
import com.openhack.domain.File;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@RegisterForReflection
public class EmployeeDTO implements Serializable {
    private Long id;

    private String identifier;

    private String name;

    private String encryptionKey;

    private Boolean enabled;

    private Instant createdAt;

    private Boolean deletion;

//    private List<File> files;
//
//    private List<Embeddings> embeddings;
}
