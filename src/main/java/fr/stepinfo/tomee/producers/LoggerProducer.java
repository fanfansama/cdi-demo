package fr.stepinfo.tomee.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

@ApplicationScoped
public class LoggerProducer {
    @Produces // not serializable so use it with caution
    public Logger produces(final InjectionPoint ip) {
        return Logger.getLogger(ip.getBean().getBeanClass().getName());
    }
}
