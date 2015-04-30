package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.model.ContentEntity;
import fr.talanlab.cdidemo.jpa.service.ContentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@ApplicationScoped
@Path("user")
public class UserResource {

    @Inject
    private ContentService contentService;

    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getUserByContentId() {
        return contentService.getUsersCountGroupedByContentId();
    }

    @GET
    @Path("like/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContentEntity iLikeIt(@PathParam("id") final String contentId) {
        return contentService.likeContent(contentId);
    }

    @DELETE
    @Path("like/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContentEntity iDontLikeAnymoreIt(@PathParam("id") final String contentId) {
        return contentService.unlikeContent(contentId);
    }

}
