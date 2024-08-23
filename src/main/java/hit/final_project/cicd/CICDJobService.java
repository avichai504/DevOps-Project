package hit.final_project.cicd;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hit.final_project.dto.CICDJobDTO;

@Service
public class CICDJobService {

    private final CICDJobRepository cicdJobRepository;
    private static final Logger logger = LoggerFactory.getLogger(CICDJobService.class);

    public CICDJobService(CICDJobRepository cicdJobRepository) {
        this.cicdJobRepository = cicdJobRepository;
    }

    public List<CICDJob> getAllJobs() {
        return cicdJobRepository.findAll();
    }

    public CICDJob getJobById(Long id) {
        return cicdJobRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Job not found with ID {}", id);
                    return new JobNotFoundException("Job not found with ID " + id);
                });
    }

    public CICDJob createJob(CICDJobDTO cicdJobDTO) {
        CICDJob cicdJob = new CICDJob();
        cicdJob.setJobName(cicdJobDTO.getJobName());
        cicdJob.setStatus(cicdJobDTO.getStatus());
        cicdJob.setJobType(cicdJobDTO.getJobType());
        cicdJob.setCreatedAt(LocalDateTime.now());
        cicdJob.setUpdatedAt(LocalDateTime.now());

        CICDJob savedJob = cicdJobRepository.save(cicdJob);
        logger.info("CICD Job has been created: {}", savedJob.getJobName());
        return savedJob;
    }

    public CICDJob updateJob(Long id, CICDJobDTO cicdJobDTO) {
        CICDJob cicdJob = cicdJobRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Job not found with ID {}", id);
                    return new JobNotFoundException("Job not found with ID " + id);
                });
        cicdJob.setJobName(cicdJobDTO.getJobName());
        cicdJob.setStatus(cicdJobDTO.getStatus());
        cicdJob.setJobType(cicdJobDTO.getJobType());
        cicdJob.setUpdatedAt(LocalDateTime.now());
        return cicdJobRepository.save(cicdJob);
    }

    public void deleteJob(Long id) {
        CICDJob cicdJob = cicdJobRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Job not found with ID {}", id);
                    return new JobNotFoundException("Job not found with ID " + id);
                });
        cicdJobRepository.delete(cicdJob);
        logger.info("CICD Job with ID {} has been deleted", id);
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
