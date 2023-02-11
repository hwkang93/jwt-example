package study.jwt.jwtexample.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        authException.printStackTrace();

        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        if (authException instanceof BadCredentialsException) {
            setResponse(response, authException.getMessage());
        } else if (authException instanceof LockedException) {
            setResponse(response, "사용자 계정이 잠겨 있습니다.");
        } else if (authException instanceof DisabledException) {
            setResponse(response, "사용자 계정이 비활성화 되어 있습니다.");
        } else {
            setResponse(response, "인증에 실패하였습니다.");
        }
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/text;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(message);
    }

}
