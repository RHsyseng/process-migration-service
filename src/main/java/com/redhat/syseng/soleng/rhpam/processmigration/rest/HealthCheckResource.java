package com.redhat.syseng.soleng.rhpam.processmigration.rest;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.redhat.syseng.soleng.rhpam.processmigration.service.CredentialsProviderFactory;
import com.redhat.syseng.soleng.rhpam.processmigration.service.KieService;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.CredentialsProvider;
import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckResource {

    @Inject
    private KieService kieService;

    @GET
    @Path("/readiness")
    @Health
    public HealthStatus checkReadiness() {
        return HealthStatus.named("readiness").up().withAttribute("date", new Date().toString());
    }

    @GET
    @Path("/liveness")
    @Health
    public HealthStatus checkLiveness(@Context HttpHeaders headers) {
        CredentialsProvider credProvider = CredentialsProviderFactory.getProvider(headers.getHeaderString(HttpHeaders.AUTHORIZATION));
        ServiceResponse<KieServerStateInfo> serverState = kieService.getServerState(credProvider);
        HealthStatus status = HealthStatus.named("liveness");
        if (serverState == null || !ResponseType.SUCCESS.equals(serverState.getType())) {
            status = status.down();
        } else {
            status = status.up();
        }
        if (serverState != null) {
            status = status.withAttribute("KieServer", serverState.getMsg());
        }
        return status.withAttribute("date", new Date().toString());

    }
}
