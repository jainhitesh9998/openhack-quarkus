package com.openhack.repository;

import com.openhack.domain.Embeddings;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class EmbeddingsRepository implements PanacheRepositoryBase<Embeddings, Long> {
    public Optional<Embeddings> findByIdentifierOptional(Long identifier){
        return find("employee_id", identifier).stream().findFirst();
    }

}
