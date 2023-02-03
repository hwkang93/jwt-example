package study.jwt.jwtexample.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class SecurityUtil {

    public static final String AUTHORITIES_KEY = "auth";

    public static final String API_KEY = "apiKey";

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }

    public static Collection<GrantedAuthority> createAuthoritiesBy(String auth, String apiKey) {
        GrantedAuthority authAuthority = new SimpleGrantedAuthority(auth);
        GrantedAuthority apiKeyAuthority = new SimpleGrantedAuthority(apiKey);

        Collection<GrantedAuthority> authorityList = new ArrayList<>();
        //list.get(0) -> auth
        authorityList.add(authAuthority);
        //list.get(1) -> api key
        authorityList.add(apiKeyAuthority);

        return Collections.unmodifiableCollection(authorityList);
    }
}
