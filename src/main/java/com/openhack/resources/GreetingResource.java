package com.openhack.resources;

import com.openhack.domain.Person;
import com.openhack.repository.PersonRepository;
import io.quarkus.security.Authenticated;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hello")
//@OidcClientFilter
@Authenticated
public class GreetingResource {
    @Inject
    PersonRepository personRepository;

    @GET
    @Path("/world")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return String.valueOf(personRepository.count());
    }

    @GET
    @Path("/error")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public String error() {
        throw new NullPointerException("there is some error here");
//        return String.valueOf(personRepository.count());
    }

    @GET
    @Path("/internal")
    @Produces(MediaType.APPLICATION_JSON)
    public String internal() {
        throw new IndexOutOfBoundsException("there is some error here");
//        return String.valueOf(personRepository.count());
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Person person() {
        return personRepository.findById(1L);
    }

    @POST
    @Path("/data")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person save(Person person) {
        personRepository.persist(person);
        return person;
    }
}