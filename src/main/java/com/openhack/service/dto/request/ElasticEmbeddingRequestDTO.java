package com.openhack.service.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ElasticEmbeddingRequestDTO implements Serializable {
    private Long id;
    List<Double> face_vector;
}
