package com.redhat.syseng.soleng.rhpam.processmigration.rest;

import java.text.ParseException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.MigrationDefinition;
import com.redhat.syseng.soleng.rhpam.processmigration.model.ProcessInstanceMigration;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;

@Path("/migrations")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MigrationResource {

    @Inject
    private MigrationService migrationService;

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    public Response findAll() {
        return Response.ok(migrationService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Migration migration = migrationService.get(id);
        if (migration == null) {
            return getMigrationNotFound(id);
        }
        return Response.ok(migration).build();
    }

    @GET
    @Path("/{id}/results")
    public Response getResults(@PathParam("id") Long id) {
        List<ProcessInstanceMigration> results = migrationService.getResults(id);
        if (results == null) {
            return getMigrationNotFound(id);
        }
        return Response.ok(results).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response submit(MigrationDefinition migration) throws NamingException, ParseException {
        return Response.ok(migrationService.submit(migration)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Long id, MigrationDefinition migration) {
        if (migrationService.get(id) == null) {
            return getMigrationNotFound(id);
        }
        // TODO: Support reschedule migrations
        return Response.notModified().build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") Long id) {
        Migration migration = migrationService.cancel(id);
        if (migration == null) {
            return getMigrationNotFound(id);
        }
        return Response.ok(migration).build();
    }

    private Response getMigrationNotFound(Long id) {
        return Response.status(Status.NOT_FOUND)
                       .entity(String.format("{\"message\": \"Migration with id %s does not exist\"}", id)).build();
    }

}
