package study.jwt.jwtexample.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.jwt.jwtexample.auth.User;
import study.jwt.jwtexample.auth.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Deprecated
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${api.key}")
    private String apiKey;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername :: " + username);

        return userRepository.findByUserId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
     *
     */
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(apiKey);

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );

    }
}
