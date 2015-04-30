package fr.stepinfo.tomee.mock;

import fr.stepinfo.tomee.jpa.service.ContentService;
import org.apache.openejb.core.security.SecurityServiceImpl;
import org.apache.openejb.junit.ApplicationRule;
import org.apache.openejb.junit.ContainerRule;
import org.apache.openejb.spi.SecurityService;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Component;
import org.apache.openejb.testing.ContainerProperties;
import org.apache.openejb.testing.Default;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.RandomPort;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import java.net.URL;
import java.security.Principal;

public class BaseTestConfig {
    protected final ContainerRule container = new ContainerRule(new Container());

    protected final ApplicationRule app = new ApplicationRule(new JavaEEEasy());

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(container)
            .around(app);

    @Component
    public SecurityService securityService() {
        return new SecurityServiceImpl() {
            @Override
            public Principal getCallerPrincipal() {
                return new User("name");
            }
        };
    }

    @Default
    @Classes(context = "app", cdi = true, excludes = "fr.talanlab.cdidemo.mock")
    public static class JavaEEEasy {

        @Inject
        private ContentService contentService;

        public ContentService getContentService() {
            return contentService;
        }

    }

    @EnableServices("jaxrs")
    @ContainerProperties({
            @ContainerProperties.Property(name = "cxf-rs.auth", value = "BASIC") // GET are accepted even if not protected :)
    })
    public static class Container {
        @RandomPort("http")
        private URL root;

        public URL getRoot() {
            return root;
        }
    }

}
