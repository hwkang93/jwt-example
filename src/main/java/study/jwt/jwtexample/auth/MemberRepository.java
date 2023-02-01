package study.jwt.jwtexample.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final PasswordEncoder passwordEncoder;

    private List<Member> memberList = new ArrayList<>();

    @PostConstruct
    public void init() {
        String encryptPassword = passwordEncoder.encode("wavus1234!");
        memberList.add(new Member(1L, "hwkang", encryptPassword, Authority.ROLE_USER, "API KEY 1"));
        memberList.add(new Member(1L, "geontest", encryptPassword, Authority.ROLE_ADMIN, "API KEY 2"));
    }

    public Optional<Member> findByEmail(String email) {
        return memberList.stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    public boolean existsByEmail(String email) {
        return memberList.stream().anyMatch(member -> member.getEmail().equals(email));
    }

    public Optional<Member> findById(Long id) {
        return memberList.stream().filter(member -> member.getId() == id).findFirst();
    }

    public Member save(Member member) {
        memberList.add(member);
        return member;
    }
}
