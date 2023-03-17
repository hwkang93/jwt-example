package study.jwt.jwtexample.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        //TODO : SMT API 요청 보내기
        boolean callApiResult = true;

        if(callApiResult) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(apiKey);
            return new UsernamePasswordAuthenticationToken(name, password, Collections.singleton(grantedAuthority));
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
