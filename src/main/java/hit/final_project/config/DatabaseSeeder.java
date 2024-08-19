package hit.final_project.config;

import hit.final_project.cicd.CICDJob;
import hit.final_project.cicd.CICDJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DatabaseSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Bean
    CommandLineRunner initDatabase(CICDJobRepository cicdJobRepository) {
        return args -> {
            logger.info("Seeding database with initial data...");

            // Seeding CI/CD jobs
            CICDJob job1 = new CICDJob("Build Project", "SUCCESS", LocalDateTime.now(), LocalDateTime.now(), "BUILD");
            CICDJob job2 = new CICDJob("Deploy Project", "PENDING", LocalDateTime.now(), LocalDateTime.now(), "DEPLOY");

            cicdJobRepository.save(job1);
            logger.info("Created CI/CD Job: {}", job1.getJobName());

            cicdJobRepository.save(job2);
            logger.info("Created CI/CD Job: {}", job2.getJobName());

            logger.info("Database seeding completed.");
        };
    }
}
