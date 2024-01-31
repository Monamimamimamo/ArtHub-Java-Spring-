package authorization;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class RefreshAspect {
    @Around("@annotation(authorization.GetRefresh)")
    public Object getRefresh(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwtToken = request.getHeader("Authorization").substring(7);
        request.setAttribute("refresh", jwtToken);

        return joinPoint.proceed();
    }
}
