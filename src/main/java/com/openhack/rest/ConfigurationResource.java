package com.openhack.rest;

import com.openhack.service.ConfigurationService;
import com.openhack.service.dto.ConfigurationDTO;
import org.slf4j.Logger;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/api/configuration")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ConfigurationResource {
    private final ConfigurationService configurationService;
    private final Logger LOG;


    public ConfigurationResource(ConfigurationService configurationService, Logger log) {
        this.configurationService = configurationService;
        LOG = log;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public Response getAll(){
        return Response.status(200).entity(configurationService.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public Response save(ConfigurationDTO configurationDTO){
        return Response.status(201).entity(configurationService.save(configurationDTO)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public Response update(ConfigurationDTO configurationDTO){
        return Response.status(Response.Status.fromStatusCode(200)).entity(configurationService.update(configurationDTO)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{identifier}")
    public Response getByDeviceIdendifier(@PathParam("identifier") String identifier){
        Optional<ConfigurationDTO> configurationDTO = configurationService.getConfigByIdentifier(identifier);
        return !configurationDTO.isEmpty() ? Response.ok(configurationDTO.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/analytics")
    public  Response getAnalytics(){

        return Response.ok().entity(configurationService.getAnalytics()).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        configurationService.delete(id);
        return Response.noContent().build();
    }

}
