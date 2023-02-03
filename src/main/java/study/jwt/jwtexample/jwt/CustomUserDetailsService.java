package study.jwt.jwtexample.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.jwt.jwtexample.auth.Member;
import study.jwt.jwtexample.auth.MemberRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
     *
     * authorities
     *  - 0 : 값은 권한 코드
     *  - 1 : Api key
     * @param member
     * @return
     */
    private UserDetails createUserDetails(Member member) {
        String auth = member.getAuthority().name();
        String apiKey = member.getApiKey();

        Collection<GrantedAuthority> authorities = SecurityUtil.createAuthoritiesBy(auth, apiKey);

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                authorities
        );

    }
}
