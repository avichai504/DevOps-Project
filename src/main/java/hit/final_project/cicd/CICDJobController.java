// src/main/java/hit/final_project/cicd/CICDJobController.java

package hit.final_project.cicd;

import hit.final_project.dto.CICDJobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class CICDJobController {

    private final CICDJobService cicdJobService;

    @Autowired
    public CICDJobController(CICDJobService cicdJobService) {
        this.cicdJobService = cicdJobService;
    }

    @GetMapping
    public ResponseEntity<List<CICDJob>> getAllJobs() {
        return ResponseEntity.ok(cicdJobService.getAllJobs());
    }

    @PostMapping
    public ResponseEntity<CICDJob> createJob(@RequestBody CICDJobDTO jobDTO) {
        CICDJob newJob = cicdJobService.createJob(jobDTO);
        return new ResponseEntity<>(newJob, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CICDJob> getJobById(@PathVariable Long id) {
        return cicdJobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CICDJob> updateJob(@PathVariable Long id, @RequestBody CICDJobDTO jobDTO) {
        try {
            CICDJob updatedJob = cicdJobService.updateJob(id, jobDTO);
            return ResponseEntity.ok(updatedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        try {
            cicdJobService.deleteJob(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CICDJob>> getJobsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(cicdJobService.getJobsByStatus(status));
    }

    @GetMapping("/jobType/{jobType}")
    public ResponseEntity<List<CICDJob>> getJobsByJobType(@PathVariable String jobType) {
        return ResponseEntity.ok(cicdJobService.getJobsByJobType(jobType));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CICDJob>> getJobsByDateRange(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(cicdJobService.getJobsByDateRange(startDate, endDate));
    }
}
