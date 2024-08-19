package hit.final_project.cicdServiceTest;

import hit.final_project.cicd.CICDJobRepository;
import hit.final_project.cicd.CICDJobService;
import hit.final_project.dto.CICDJobDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CICDJobExceptionTest {

    @Mock
    private CICDJobRepository cicdJobRepository;

    @InjectMocks
    private CICDJobService cicdJobService;

    private CICDJobDTO cicdJobDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cicdJobDTO = new CICDJobDTO();
        cicdJobDTO.setJobName("Test Job");
        cicdJobDTO.setStatus("Pending");
        cicdJobDTO.setJobType("Build");
    }

    @Test
    void testDeleteNonExistentJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cicdJobService.deleteJob(1L));
    }

    @Test
    void testUpdateNonExistentJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cicdJobService.updateJob(1L, cicdJobDTO));
    }

    @Test
    void testGetNonExistentJob() {
        when(cicdJobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cicdJobService.getJobById(1L));
    }
}
