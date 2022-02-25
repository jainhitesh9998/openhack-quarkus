package com.openhack.service.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@RegisterForReflection
public class ElasticSearchRequestDTO implements Serializable {
    private List<Double> query_vector;
}
