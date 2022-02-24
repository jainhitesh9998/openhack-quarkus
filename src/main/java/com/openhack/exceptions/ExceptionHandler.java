package com.openhack.exceptions;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class ExceptionHandler implements ExceptionMapper<RuntimeException> {
    @Inject
    Logger LOG;

    @Override
    public Response toResponse(RuntimeException e) {
        if(e.getClass() == NullPointerException.class){
            LOG.error("Some error occurred");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessage(
                    LocalDateTime.now(),
                    Response.Status.BAD_REQUEST,
                    e.getMessage(),
                    "",
                    ""

            )).build();
        } else {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(
                    LocalDateTime.now(),
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "",
                    "",
                    ""

            )).build();
        }
    }
}
