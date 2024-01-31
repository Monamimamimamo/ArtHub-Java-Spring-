package authorization;

import User.Users;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import User.UsersRepo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UsersRepo usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UsersRepo userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> signUp(String email, String password, String login) {

        Users user = new Users();
        user.setEmail(email);
        user.setLogin(login);
        user.setHash(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        usersRepository.save(user);

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        map.put("email", email);


        return map;
    }

    public Map<String, Object> signIn(String email, String password, String login) {
        try {
            Users user = usersRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("User not found");
            }
            if(!passwordEncoder.matches(password, user.getHash())) {
                throw new Exception("Invalid password");
            }
            String accessToken = jwtUtil.generateAccessToken(user);
            user.setUpdatedAt(LocalDateTime.now());
            updateRefreshTokenHash(user.getId(), user.getRefreshToken());

            Map<String, Object> map = new HashMap<>();
            map.put("access_token", accessToken);
            map.put("refresh_token", user.getRefreshToken());
            map.put("email", email);

            return map;
        } catch (Exception e) {
            // handle exception here
            e.printStackTrace();
            return null;
        }
    }

    public void logOut (HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("userId");
        Users user = usersRepository.findById(userId);
        user.setRefreshToken(null);
        System.out.println(user.getRefreshToken());
        usersRepository.save(user);
    }

    public Map<String, Object> refresh(HttpServletRequest request) throws Exception {
        Integer userId = (Integer) request.getAttribute("userId");
        String refresh = (String) request.getAttribute("refresh");
        Users user = usersRepository.findById(userId);
        if (user.getRefreshToken() == null) {
            throw new Exception("The user is not logged in");
        }
        if(!refresh.equals(user.getRefreshToken())) {
            throw new Exception("Access is denied");
        }
        String accessToken = jwtUtil.generateAccessToken(user);

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("email", user.getEmail());

        return map;
    }


    public void updateRefreshTokenHash(Integer userId, String refreshToken) {
        Users user = usersRepository.findById(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        usersRepository.save(user);
    }
}
