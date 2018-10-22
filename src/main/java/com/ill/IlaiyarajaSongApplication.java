package com.ill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IlaiyarajaSongApplication implements ApplicationRunner {
    //private static final Logger logger = LoggerFactory.getLogger(IlaiyarajaSongApplication.class);
    private static final Logger logger = LogManager.getLogger(IlaiyarajaSongApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IlaiyarajaSongApplication.class, args);
        logger.debug("--Rakesh Application Started--");
    }

    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
    }
}
