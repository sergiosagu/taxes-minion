package com.github.sergiosagu.taxesminion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaxesminionApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(TaxesminionApplication.class);

	public static void main(String[] args) {
		LOG.info("main starts...");
		SpringApplication.run(TaxesminionApplication.class, args);
		LOG.info("main stops...");
	}

	@Override
	public void run(String... args) throws Exception {
        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
	}

}
