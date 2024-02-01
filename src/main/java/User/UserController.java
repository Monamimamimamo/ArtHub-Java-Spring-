package User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/brushes")
    @GetUserId
    public ResponseEntity<?> getUsersBrushes(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getUsersBrushes(request), HttpStatus.OK);
    }

    @GetMapping("/references")
    @GetUserId
    public ResponseEntity<?> getUsersReferences(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getUsersReferences(request), HttpStatus.OK);
    }

    @GetMapping("/programs")
    @GetUserId
    public ResponseEntity<?> getUsersPrograms(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getUsersPrograms(request), HttpStatus.OK);
    }

    @GetMapping("/tutorials")
    @GetUserId
    public ResponseEntity<?> getUsersTutorials(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getUsersTutorials(request), HttpStatus.OK);
    }

}
