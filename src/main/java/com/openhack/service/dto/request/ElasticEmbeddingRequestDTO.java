package com.openhack.service.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@RegisterForReflection
public class ElasticEmbeddingRequestDTO implements Serializable {
    private Long id;
    List<Double> face_vector;
}
