package hit.final_project.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User entity
 * This controller handles HTTP requests and responses for user interaction
 * and operations
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets all the users in the User table using the UserService
     * @return an HTTP Response including a JSON with an Array of JSONs,
     * representing all the users in our database.
     * Successful requests results in status code 200
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User saveUser = userService.saveUser(user);
        // NOT good practice. alternatively return status 201 CREATED
        return ResponseEntity.ok(user);
    }

    // user performs HTTP GET request in the form of /api/user/20
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        return userService.findUserById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // /api/users/20
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();  // Returns 404 if the user is not found
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();  // Returns 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();  // Returns 404 Not Found
        }
    }



}
