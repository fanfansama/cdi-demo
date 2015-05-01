package fr.stepinfo.tomee.rest;

import fr.stepinfo.tomee.jpa.model.ContentEntity;
import fr.stepinfo.tomee.jpa.service.ContentService;
import fr.stepinfo.tomee.mock.BaseTestConfig;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.Assert.assertEquals;

public class UserResourceTest extends BaseTestConfig {

    public static final String JAVA_JEE = "java_jee";


    /*@Test
    public void getContents() {
        assertConferences(
                ClientBuilder.newBuilder().build()
                        .register(new JohnzonProvider())
                        .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/conferences")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get(new GenericType<List<content>>() {
                        }));
    } */

    @Test
    public void likeThenDislike() {
        final String contentId = JAVA_JEE;
        final ContentService contentService = app.getInstance(TomeeDemo.class).getContentService();

        // cf: users.properties dans openejb-core.jar
        final String authorization = "Basic " + printBase64Binary("bob:eponge".getBytes());
        final String authorization2 = "Basic " + printBase64Binary("jonathan:secret".getBytes());
        final WebTarget client = ClientBuilder.newBuilder().build()
                .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/user/like");

        {
            contentService.save(new ContentEntity(contentId));
            final ContentEntity content = contentService.findById(contentId);
            assertEquals(0, contentService.count(content));
        }
        {
            Response response = client.path("{content}").resolveTemplate("content", JAVA_JEE)
                    .request()
                    .header("Authorization", authorization)
                    .get();
            assertEquals(response.getStatus(), 200);
            assertEquals(1, contentService.count(contentService.findById(contentId)));
        }
        {
            Response response = client.path("{content}").resolveTemplate("content", JAVA_JEE)
                    .request()
                    .header("Authorization", authorization2)
                    .get();
            assertEquals(response.getStatus(), 200);
            assertEquals(2, contentService.count(contentService.findById(contentId)));
        }
        {
            Response response = client.path("{content}").resolveTemplate("content", JAVA_JEE)
                    .request()
                    .header("Authorization", authorization)
                    .delete();
            assertEquals(response.getStatus(), 200);
            assertEquals(1, contentService.count(contentService.findById(contentId)));
        }
    }
}