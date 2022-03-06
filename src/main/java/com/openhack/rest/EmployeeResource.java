package com.openhack.rest;

import com.openhack.service.AttendanceService;
import com.openhack.service.EmployeeService;
import com.openhack.service.dto.EmployeeDTO;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.reactive.MultipartForm;
import org.slf4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("/api/employee")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class EmployeeResource {

    private final EmployeeService employeeService;

    private final Logger LOG;

    private final JsonWebToken jsonWebToken;

    private final AttendanceService attendanceService;



    public EmployeeResource(EmployeeService employeeService, Logger log, JsonWebToken jsonWebToken, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        LOG = log;
        this.jsonWebToken = jsonWebToken;
        this.attendanceService = attendanceService;
    }

    @GET
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "find all employees",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = EmployeeDTO.class))
                    )
            }
    )
    public Response getAllEmployees(){
        return Response.ok(employeeService.findAll()).build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/profile/{employeeId}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Get Customer by employeeid",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = EmployeeDTO.class))),
                    @APIResponse(
                            responseCode = "404",
                            description = "No Employee found for employeeid provided",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public Response getByEmployeeId(@PathParam("employeeId") Long employeeId){
        Optional<EmployeeDTO> employeeDTO = employeeService.findById(employeeId);
        return !employeeDTO.isEmpty() ? Response.ok(employeeDTO).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "Employee Created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.OBJECT, implementation = EmployeeDTO.class))),
                    @APIResponse(
                            responseCode = "400",
                            description = "Customer already exists for id",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public Response createEmployee(EmployeeDTO employeeDTO){
        EmployeeDTO response = employeeService.save(employeeDTO);
        LOG.info("user name {}", jsonWebToken.getClaim("preferred_username").toString() );
//        LOG.info("user info {}", info.get("sub"));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/photo")
    public Response uploadFile(@MultipartForm MultipartFormDataInput input) throws IOException {
        LOG.info("Entered the upload file function {}", input.toString());
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<String> fileNames = new ArrayList<>();
        LOG.info("{} ",String.join(" " ,uploadForm.keySet()));
        List<InputPart> inputParts = uploadForm.get("file");

        List<Double> doubles = employeeService.retriveEmbedding(input);

        return Response.ok().build();
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/authenticate")
    public Response authenticate(@MultipartForm MultipartFormDataInput input, @QueryParam("device-identifier") String mac_address, @QueryParam("temperature") Double temperature) throws IOException {
        return Response.ok().entity(employeeService.authenticate(input, mac_address, temperature)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attendance")
    public Response getAttendanceOfEmployee(){
        return Response.status(200).entity(employeeService.getAttendanceRecordsForEmployee()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public Response update(EmployeeDTO employeeDTO){
        return Response.ok().entity(employeeService.update(employeeDTO)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profile")
    public Response getProfile(){
        return Response.status(200).entity(employeeService.getProfile()).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attendance/{employee-id}")
    public Response getAttendanceOfEmployee(@PathParam("employee-id") Long employeeId){
        return Response.status(200).entity(attendanceService.findByIdentifier(employeeId)).build();
    }


}
