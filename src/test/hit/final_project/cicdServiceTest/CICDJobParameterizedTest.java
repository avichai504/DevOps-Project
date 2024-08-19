package hit.final_project.cicdServiceTest;

import hit.final_project.cicd.CICDJob;
import hit.final_project.cicd.CICDJobRepository;
import hit.final_project.cicd.CICDJobService;
import hit.final_project.dto.CICDJobDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CICDJobParameterizedTest {

    @Mock
    private CICDJobRepository cicdJobRepository;

    @InjectMocks
    private CICDJobService cicdJobService;

    private CICDJob cicdJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cicdJob = new CICDJob();
        cicdJob.setId(1L);
        cicdJob.setJobName("Test Job");
        cicdJob.setStatus("Pending");
        cicdJob.setJobType("Build");
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());
    }

    @ParameterizedTest
    @CsvSource({
            "Pending, Build",
            "In Progress, Deploy",
            "Completed, Test"
    })
    void testAddJobWithVariousStatuses(String status, String jobType) {
        CICDJobDTO cicdJobDTO = new CICDJobDTO();
        cicdJobDTO.setJobName("Test Job");
        cicdJobDTO.setStatus(status);
        cicdJobDTO.setJobType(jobType);

        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName(cicdJobDTO.getJobName());
        cicdJob.setStatus(cicdJobDTO.getStatus());
        cicdJob.setJobType(cicdJobDTO.getJobType());

        when(cicdJobRepository.save(any(CICDJob.class))).thenReturn(cicdJob);

        CICDJob createdJob = cicdJobService.createJob(cicdJobDTO);

        assertNotNull(createdJob);
        assertEquals("Test Job", createdJob.getJobName());
        assertEquals(status, createdJob.getStatus());
        assertEquals(jobType, createdJob.getJobType());
    }


    @ParameterizedTest
    @CsvSource({
            "1, Test Job 1",
            "2, Test Job 2",
            "3, Test Job 3"
    })
    void testGetJobByDifferentIds(Long id, String expectedJobName) {
        cicdJob.setId(id);
        cicdJob.setJobName(expectedJobName);

        when(cicdJobRepository.findById(id)).thenReturn(Optional.of(cicdJob));

        CICDJob retrievedJob = cicdJobService.getJobById(id);

        assertNotNull(retrievedJob);
        assertEquals(expectedJobName, retrievedJob.getJobName());
    }
}