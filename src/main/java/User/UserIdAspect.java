package User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import User.Users;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import User.UsersRepo;
import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class UserIdAspect {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UsersRepo userRepository;

    @Around("@annotation(User.GetUserId)")
    public Object getUserId(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwtToken = request.getHeader("Authorization").substring(7);
        String sub = extractUserFromJwt(jwtToken);
        Integer userId = findUserBySub(sub).getId();
        request.setAttribute("userId", userId);

        return joinPoint.proceed();
    }

    private String extractUserFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();
    }

    private Users findUserBySub(String sub) {
        return userRepository.findByEmail(sub);
    }

}
