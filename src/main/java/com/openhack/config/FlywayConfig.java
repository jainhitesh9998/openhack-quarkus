package com.openhack.config;

import io.quarkus.flyway.FlywayDataSource;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class FlywayConfig {

    @Inject
    Logger logger;

    @Inject
    Flyway flyway;

    public void checkMigration() {
        // Use the flyway instance manually
        flyway.clean();
        flyway.migrate();
        // This will print 1.0.0
        logger.info("Running Flyway Migration scripts");
    }
}
