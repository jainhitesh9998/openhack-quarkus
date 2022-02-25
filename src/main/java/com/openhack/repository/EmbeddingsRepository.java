package com.openhack.repository;

import com.openhack.domain.Embeddings;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmbeddingsRepository implements PanacheRepositoryBase<Embeddings, Long> {

}
