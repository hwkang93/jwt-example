package study.jwt.jwtexample.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final PasswordEncoder passwordEncoder;

    private List<User> userList = new ArrayList<>();

    @PostConstruct
    public void init() {
        String encryptPassword = passwordEncoder.encode("wavus1234!");
        userList.add(new User(1L, "hwkang", encryptPassword,  "API KEY 1"));
        userList.add(new User(1L, "geontest", encryptPassword, "API KEY 2"));
    }

    public Optional<User> findByEmail(String email) {
        return userList.stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    public boolean existsByEmail(String email) {
        return userList.stream().anyMatch(member -> member.getEmail().equals(email));
    }

    public Optional<User> findById(Long id) {
        return userList.stream().filter(member -> member.getId() == id).findFirst();
    }

    public User save(User user) {
        userList.add(user);
        return user;
    }
}
