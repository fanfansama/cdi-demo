package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.service.ContentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.Collection;

@ApplicationScoped
@Path("content")
public class ContentResource {

    @Inject
    private ContentService contentService;

    @Inject
    private Principal principal;

    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getlikedContentIds() {
        return contentService.getLikedContentIdsForUser(principal.getName());
    }

}
