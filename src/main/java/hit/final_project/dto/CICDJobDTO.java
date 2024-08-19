
package hit.final_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CICDJobDTO {
    private String jobName;
    private String status;
    private String jobType;
    private Date createdAt;
    private Date updatedAt;
}
