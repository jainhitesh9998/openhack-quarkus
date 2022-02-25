package com.openhack.service.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ElasticEmbeddingRequestDTO {
    private Long id;
    List<Double> face_vector;
}
