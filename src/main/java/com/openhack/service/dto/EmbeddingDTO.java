package com.openhack.service.dto;

import com.openhack.domain.Employee;
import io.quarkiverse.hibernate.types.json.JsonTypes;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.List;

@RegisterForReflection
@Data
public class EmbeddingDTO implements Serializable {
    private Long id;
    private List<Double> embedding;
    private Employee employee;
}
