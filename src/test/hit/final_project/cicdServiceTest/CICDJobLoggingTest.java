package hit.final_project.cicdServiceTest;

import hit.final_project.cicd.CICDJob;
import hit.final_project.cicd.CICDJobRepository;
import hit.final_project.cicd.CICDJobService;
import hit.final_project.dto.CICDJobDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class CICDJobLoggingTest {

    @Mock
    private CICDJobRepository cicdJobRepository;

    @InjectMocks
    private CICDJobService cicdJobService;

    private final TestLogger testLogger = TestLoggerFactory.getTestLogger(CICDJobService.class);

    private CICDJobDTO cicdJobDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cicdJobDTO = new CICDJobDTO();
        cicdJobDTO.setJobName("Build Project");
        cicdJobDTO.setStatus("Pending");
        cicdJobDTO.setJobType("Build");

        testLogger.clear();
    }

    @Test
    void testLogWhenJobCreated() {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName("Build Project");

        when(cicdJobRepository.save(any(CICDJob.class))).thenReturn(cicdJob);

        CICDJob savedJob = cicdJobService.createJob(cicdJobDTO);

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size());
        assertEquals("CICD Job has been created: Build Project", loggingEvents.get(0).getMessage());
    }

    @Test
    void testLogWhenJobRetrieved() {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName("Deploy Project");
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.of(cicdJob));

        cicdJobService.getJobById(1L);

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size());
        assertEquals("Retrieved job with ID 1", loggingEvents.get(0).getMessage());
    }

    @Test
    void testLogWhenJobNotFound() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cicdJobService.getJobById(1L));

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size());
        assertEquals("Job not found with ID 1", loggingEvents.get(0).getMessage());
    }
}
