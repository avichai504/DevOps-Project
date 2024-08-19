package hit.final_project.cicd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CICDJobRepository extends JpaRepository<CICDJob, Long> {
    List<CICDJob> findByStatus(String status);
    List<CICDJob> findByJobType(String jobType);
    List<CICDJob> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
