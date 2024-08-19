
package hit.final_project.cicd;

import hit.final_project.dto.CICDJobDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CICDJobService {

    private final CICDJobRepository cicdJobRepository;

    public CICDJobService(CICDJobRepository cicdJobRepository) {
        this.cicdJobRepository = cicdJobRepository;
    }

    public List<CICDJob> getAllJobs() {
        return cicdJobRepository.findAll();
    }

    public Optional<CICDJob> getJobById(Long id) {
        return cicdJobRepository.findById(id);
    }

    public CICDJob createJob(CICDJobDTO cicdJobDTO) {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName(cicdJobDTO.getJobName());
        cicdJob.setStatus(cicdJobDTO.getStatus());
        cicdJob.setJobType(cicdJobDTO.getJobType());
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());
        return cicdJobRepository.save(cicdJob);
    }

    public CICDJob updateJob(Long id, CICDJobDTO cicdJobDTO) {
        CICDJob cicdJob = cicdJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        cicdJob.setJobName(cicdJobDTO.getJobName());
        cicdJob.setStatus(cicdJobDTO.getStatus());
        cicdJob.setJobType(cicdJobDTO.getJobType());
        cicdJob.setUpdatedAt(LocalDateTime.now());
        return cicdJobRepository.save(cicdJob);
    }

    public void deleteJob(Long id) {
        CICDJob cicdJob = cicdJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        cicdJobRepository.delete(cicdJob);
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
