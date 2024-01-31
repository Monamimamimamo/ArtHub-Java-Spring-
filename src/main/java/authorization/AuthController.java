package authorization;

import authorization.GetRefresh;
import User.GetUserId;
import User.UserSignRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import authorization.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserSignRequest userSignUpRequest) {
        String email = userSignUpRequest.getEmail();
        String password = userSignUpRequest.getPassword();
        String login = userSignUpRequest.getLogin();
        return new ResponseEntity<>(authService.signUp(email, password, login), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody UserSignRequest userSignUpRequest) {
        String email = userSignUpRequest.getEmail();
        String password = userSignUpRequest.getPassword();
        String login = userSignUpRequest.getLogin();
        return new ResponseEntity<>(authService.signIn(email, password, login), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    @GetUserId
    public ResponseEntity<String> logOut(HttpServletRequest request) {
        authService.logOut(request);
        return ResponseEntity.ok("Successfully created program");
    }

    @PostMapping("/refresh")
    @GetUserId
    @GetRefresh
    public ResponseEntity<Map<String, Object>> refresh(HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(authService.refresh(request), HttpStatus.CREATED);
    }
}
