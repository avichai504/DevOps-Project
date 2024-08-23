package hit.final_project.cicd;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hit.final_project.dto.CICDJobDTO;

@RestController
@RequestMapping("/api/jobs")
public class CICDJobController {

    private final CICDJobService cicdJobService;

    @Autowired
    public CICDJobController(CICDJobService cicdJobService) {
        this.cicdJobService = cicdJobService;
    }

    // Get all jobs
    @GetMapping
    public ResponseEntity<List<CICDJob>> getAllJobs() {
        return ResponseEntity.ok(cicdJobService.getAllJobs());
    }

    // Create a new job
    @PostMapping
    public ResponseEntity<CICDJob> createJob(@RequestBody CICDJobDTO jobDTO) {
        CICDJob newJob = cicdJobService.createJob(jobDTO);
        return new ResponseEntity<>(newJob, HttpStatus.CREATED);
    }

    // Get a job by ID
    @GetMapping("/{id}")
    public ResponseEntity<CICDJob> getJobById(@PathVariable Long id) {
        try {
            CICDJob cicdJob = cicdJobService.getJobById(id);
            return ResponseEntity.ok(cicdJob);
        } catch (JobNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a job
    @PutMapping("/{id}")
    public ResponseEntity<CICDJob> updateJob(@PathVariable Long id, @RequestBody CICDJobDTO jobDTO) {
        try {
            CICDJob updatedJob = cicdJobService.updateJob(id, jobDTO);
            return ResponseEntity.ok(updatedJob);
        } catch (JobNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        try {
            cicdJobService.deleteJob(id);
            return ResponseEntity.noContent().build();
        } catch (JobNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get jobs by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CICDJob>> getJobsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(cicdJobService.getJobsByStatus(status));
    }

    // Get jobs by type
    @GetMapping("/jobType/{jobType}")
    public ResponseEntity<List<CICDJob>> getJobsByJobType(@PathVariable String jobType) {
        return ResponseEntity.ok(cicdJobService.getJobsByJobType(jobType));
    }

    // Get jobs by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<CICDJob>> getJobsByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(cicdJobService.getJobsByDateRange(startDate, endDate));
    }

    // Exception handler for specific exception
    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<Void> handleJobNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
