package fr.talanlab.cdidemo.jpa.service;


import fr.stepinfo.tomee.jpa.model.ContentEntity;
import fr.stepinfo.tomee.jpa.model.UserEntity;
import fr.stepinfo.tomee.jpa.service.ContentService;
import fr.stepinfo.tomee.models.event.ContentEvent;
import fr.stepinfo.tomee.producers.LoggerProducer;
import fr.talanlab.cdidemo.DbCleaner;
import org.apache.openejb.core.security.SecurityServiceImpl;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.spi.SecurityService;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Component;
import org.apache.openejb.testing.ContainerProperties;
import org.apache.openejb.testing.Descriptor;
import org.apache.openejb.testing.Descriptors;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.security.Principal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(ApplicationComposer.class)
@Classes(context = "app", cdi = true, value = {
        ContentService.class, LoggerProducer.class, DbCleaner.class
})
@Descriptors({
        @Descriptor(name = "persistence.xml", path = "src/main/webapp/WEB-INF/persistence.xml")
})
@ContainerProperties({
        @ContainerProperties.Property(name = "db", value = "new://Resource?type=DataSource"),
        @ContainerProperties.Property(name = "db.JdbcUrl", value = "jdbc:hsqldb:mem:test")
})
public class ContentServiceTest {
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;

    @Inject
    private Event<ContentEvent> conferencesEvent;

    @Inject
    private UserTransaction ut;

    @Inject
    private ContentService contentService;

    // @{see securityService()}
    private String name;

    @Test
    public void incrementLike() throws Exception {
        {
            final ContentEntity slot = new ContentEntity();
            slot.setId("incr");

            ut.begin();
            em.persist(slot);
            ut.commit();
        }
        assertEquals(0, contentService.count(em.find(ContentEntity.class, "incr")));

        for (int i = 0; i < 10; i++) {
            name = "i#" + i;
            contentService.likeContent("incr");
            assertEquals(i + 1, contentService.count(em.find(ContentEntity.class, "incr")));
        }
    }

    @Test
    public void decrementLike() throws Exception {
        {
            final ContentEntity content = new ContentEntity();
            content.setId("decr");

            ut.begin();
            em.persist(content);
            for (int i = 0; i < 5; i++) {
                final UserEntity user = new UserEntity();
                user.setName("d#" + i);
                user.getContents().add(content);
                em.persist(user);
            }
            ut.commit();
        }
        assertEquals(5, contentService.count(em.find(ContentEntity.class, "decr")));

        for (int i = 0; i < 5; i++) {
            name = "d#" + i;
            contentService.unlikeContent("decr");
            assertEquals(5 - i - 1, contentService.count(em.find(ContentEntity.class, "decr")));
        }
        for (int i = 5; i < 10; i++) {
            name = "d#" + i;
            contentService.unlikeContent("decr");
            assertEquals(0, contentService.count(em.find(ContentEntity.class, "decr")));
        }
    }

    @Test
    public void getUsersCountGroupedByContentId() throws Exception {
        ContentEntity content1 = new ContentEntity();
        content1.setId("content1");
        ContentEntity content2 = new ContentEntity();
        content2.setId("content2");
        ut.begin();
        em.persist(content1);
        em.persist(content2);
        ut.commit();

        UserEntity user1 = new UserEntity();
        user1.getContents().add(content1);
        user1.getContents().add(content2);
        user1.setName("1");

        UserEntity user2 = new UserEntity();
        user2.getContents().add(content1);
        user2.setName("2");

        ut.begin();
        em.persist(user1);
        em.persist(user2);
        ut.commit();

        Map<String, Integer> likeByContentId = contentService.getUsersCountGroupedByContentId();

        assertNotNull(likeByContentId);
        assertEquals(2, likeByContentId.size());
        assertEquals(2, (int) likeByContentId.get(content1.getId()));
        assertEquals(1, (int) likeByContentId.get(content2.getId()));


    }

    @Component
    public SecurityService securityService() {
        return new SecurityServiceImpl() {
            @Override
            public Principal getCallerPrincipal() {
                return new User(name);
            }
        };
    }


}
