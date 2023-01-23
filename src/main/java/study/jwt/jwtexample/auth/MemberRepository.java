package study.jwt.jwtexample.auth;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private List<Member> memberList = new ArrayList<>(
            Arrays.asList(new Member(1L, "hwkang", "wavus1234!", Authority.ROLE_USER)
    ));

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
