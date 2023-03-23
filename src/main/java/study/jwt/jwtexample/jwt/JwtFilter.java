package study.jwt.jwtexample.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@WebFilter
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String apiKey = this.getApiKey(authentication);
            String originalQueryString = request.getQueryString();
            String newQueryString = originalQueryString != null ? originalQueryString + "&" + "apiKey" + "=" + apiKey : "apiKey" + "=" + apiKey;

            logger.info(":: new QueryString : " + newQueryString);

            filterChain.doFilter(new JwtHttpServletRequestWrapper(request, newQueryString), response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }


    private String getApiKey(Authentication authentication) {
        //GrantedAuthority grantedAuthority = authentication.getAuthorities().stream().findFirst().get();
        //return grantedAuthority.getAuthority();

        return authentication.getAuthorities().toArray()[0].toString();
    }

    private static class JwtHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String queryString;

        public JwtHttpServletRequestWrapper(HttpServletRequest request, String queryString) {
            super(request);
            this.queryString = queryString;
        }

        @Override
        public String getQueryString() {
            return queryString;
        }
    }
}


