/**
 * Custom exception class that indicates a job was not found.
 * When thrown, this exception will result in a "404 NOT FOUND" 
 * HTTP status being returned to the client.
 *
 * This class extends RuntimeException and should be used 
 * when a requested job cannot be located in the system.
 */

package hit.final_project.cicd;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
