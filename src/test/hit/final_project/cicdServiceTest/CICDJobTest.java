package hit.final_project.cicdServiceTest;

import hit.final_project.cicd.CICDJob;
import hit.final_project.cicd.CICDJobRepository;
import hit.final_project.cicd.CICDJobService;
import hit.final_project.dto.CICDJobDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CICDJobTest {

    @Mock
    private CICDJobRepository cicdJobRepository;

    @InjectMocks
    private CICDJobService cicdJobService;

    private CICDJob cicdJob;
    private CICDJobDTO cicdJobDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cicdJobDTO = new CICDJobDTO();
        cicdJobDTO.setJobName("Test Job");
        cicdJobDTO.setStatus("Pending");
        cicdJobDTO.setJobType("Build");

        cicdJob = new CICDJob();
        cicdJob.setId(1L);
        cicdJob.setJobName("Test Job");
        cicdJob.setStatus("Pending");
        cicdJob.setJobType("Build");
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testAddJob() {
        when(cicdJobRepository.save(any(CICDJob.class))).thenReturn(cicdJob);

        CICDJob createdJob = cicdJobService.createJob(cicdJobDTO);

        assertNotNull(createdJob);
        assertEquals("Test Job", createdJob.getJobName());
    }

    @Test
    void testGetJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.of(cicdJob));

        CICDJob retrievedJob = cicdJobService.getJobById(1L);

        assertNotNull(retrievedJob);
        assertEquals("Test Job", retrievedJob.getJobName());
    }


    @Test
    void testUpdateJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.of(cicdJob));
        when(cicdJobRepository.save(any(CICDJob.class))).thenReturn(cicdJob);

        CICDJob updatedJob = cicdJobService.updateJob(1L, cicdJobDTO);

        assertNotNull(updatedJob);
        assertEquals("Test Job", updatedJob.getJobName());
        assertEquals("Build", updatedJob.getJobType());
    }

    @Test
    void testDeleteJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.of(cicdJob));
        doNothing().when(cicdJobRepository).delete(any(CICDJob.class));

        assertDoesNotThrow(() -> cicdJobService.deleteJob(1L));
    }
}
