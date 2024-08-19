package hit.final_project.cicdControllerTest;

import hit.final_project.cicd.CICDJob;
import hit.final_project.cicd.CICDJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CICDJobIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CICDJobRepository cicdJobRepository;

    @BeforeEach
    void setUp() {
        cicdJobRepository.deleteAll();
    }

    @Test
    void testCreateJob() throws Exception {
        String jobJson = "{\"jobName\":\"Integration Test Job\",\"status\":\"Pending\",\"jobType\":\"Build\"}";

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName").value("Integration Test Job"))
                .andExpect(jsonPath("$.status").value("Pending"))
                .andExpect(jsonPath("$.jobType").value("Build"));
    }

    @Test
    void testGetJob() throws Exception {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName("Integration Test Job");
        cicdJob.setStatus("Pending");
        cicdJob.setJobType("Build");
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());

        cicdJobRepository.save(cicdJob);

        mockMvc.perform(get("/jobs/" + cicdJob.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName").value("Integration Test Job"))
                .andExpect(jsonPath("$.status").value("Pending"))
                .andExpect(jsonPath("$.jobType").value("Build"));
    }

    @Test
    void testUpdateJob() throws Exception {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName("Original Job Name");
        cicdJob.setStatus("Pending");
        cicdJob.setJobType("Build");
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());

        cicdJob = cicdJobRepository.save(cicdJob);

        String updatedJobJson = "{\"jobName\":\"Updated Job Name\",\"status\":\"Completed\",\"jobType\":\"Deploy\"}";

        mockMvc.perform(put("/jobs/" + cicdJob.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJobJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName").value("Updated Job Name"))
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.jobType").value("Deploy"));
    }

    @Test
    void testDeleteJob() throws Exception {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName("Job to be Deleted");
        cicdJob.setStatus("Pending");
        cicdJob.setJobType("Build");
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());

        cicdJob = cicdJobRepository.save(cicdJob);

        mockMvc.perform(delete("/jobs/" + cicdJob.getId()))
                .andExpect(status().isOk());

        Optional<CICDJob> deletedJob = cicdJobRepository.findById(cicdJob.getId());
        assertFalse(deletedJob.isPresent());
    }
}
