package com.openhack.rest.client;


import com.openhack.service.dto.request.EmbeddingRequestDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("")
@RegisterRestClient
public interface EmbeddingsService {
    @POST
    @Path("/encode/")
    @Consumes({MediaType.APPLICATION_JSON})
    List<Double> getEmbeddings(EmbeddingRequestDTO embeddingRequestDTO);

    @POST
    @Path("/encode/")
    @Consumes({MediaType.APPLICATION_JSON})
    CompletionStage<List<Double>> sendImageForEmbeddingAsync(EmbeddingRequestDTO embeddingRequestDTO);
}
