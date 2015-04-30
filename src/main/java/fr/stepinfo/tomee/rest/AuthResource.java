package fr.stepinfo.tomee.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

@ApplicationScoped              //  bean CDI en scope singleton
@Path("auth")                   // racine de mon uri REST
public class AuthResource {

    @Inject                     // j'injecte l'utilisateur connecté
    private Principal principal;

    @GET
    @Path("whoami")             // GET /rest/auth/whoami retournera le nom de l'utilisateur connecté
    @Produces(MediaType.APPLICATION_JSON)
    public String getlikedContentIds() {
        return principal.getName();
    }
}
