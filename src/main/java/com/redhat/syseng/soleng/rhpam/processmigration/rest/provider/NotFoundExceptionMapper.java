package com.redhat.syseng.soleng.rhpam.processmigration.rest.provider;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.redhat.syseng.soleng.rhpam.processmigration.model.exceptions.PlanNotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<PlanNotFoundException> {

    @Override
    public Response toResponse(PlanNotFoundException exception) {
        JsonObject json = Json.createObjectBuilder().add("message", exception.getMessage()).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(json).type(MediaType.APPLICATION_JSON).build();
    }

}
