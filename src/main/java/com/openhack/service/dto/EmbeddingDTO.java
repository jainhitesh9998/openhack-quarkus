package com.openhack.service.dto;

import com.openhack.domain.Employee;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@RegisterForReflection
@Data
public class EmbeddingDTO implements Serializable {
    private Long id;
    private List<Double> embedding;
    private Employee employee;
}
