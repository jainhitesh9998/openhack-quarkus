package com.openhack.service.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;

@Data
@RegisterForReflection
public class EmbeddingRequestDTO implements Serializable {
    private String image;
}
