// src/main/java/hit/final_project/cicd/CICDJobService.java
package hit.final_project.cicd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service  // Mark as a Service component, allowing SpringBoot to INJECT the dependency automatically
public class CICDJobService {

    private final CICDJobRepository cicdJobRepository;

    @Autowired
    public CICDJobService(CICDJobRepository cicdJobRepository) {
        this.cicdJobRepository = cicdJobRepository;
    }

    public List<CICDJob> getAllJobs() {
        return cicdJobRepository.findAll();
    }

    public Optional<CICDJob> getJobById(Long id) {
        return cicdJobRepository.findById(id);
    }

    public CICDJob createJob(CICDJob job) {
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return cicdJobRepository.save(job);
    }

    public CICDJob updateJob(Long id, CICDJob jobDetails) {
        CICDJob job = cicdJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
        job.setJobName(jobDetails.getJobName());
        job.setStatus(jobDetails.getStatus());
        job.setJobType(jobDetails.getJobType());
        job.setUpdatedAt(LocalDateTime.now());
        return cicdJobRepository.save(job);
    }

    public void deleteJob(Long id) {
        if (cicdJobRepository.existsById(id)) {
            cicdJobRepository.deleteById(id);
        } else {
            throw new RuntimeException("Job not found with ID: " + id);
        }
    }

    public List<CICDJob> getJobsByStatus(String status) {
        return cicdJobRepository.findByStatus(status);
    }

    public List<CICDJob> getJobsByJobType(String jobType) {
        return cicdJobRepository.findByJobType(jobType);
    }

    public List<CICDJob> getJobsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return cicdJobRepository.findByCreatedAtBetween(startDate, endDate);
    }
}
