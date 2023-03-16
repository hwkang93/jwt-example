package study.jwt.jwtexample.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
/*
    private final PasswordEncoder passwordEncoder;

    private List<User> userList = new ArrayList<>();

    @PostConstruct
    public void init() {
        String encryptPassword = passwordEncoder.encode("wavus1234!");
        userList.add(new User("hwkang", "hwkang@wavus.co.kr", encryptPassword,  "API KEY 1"));
        userList.add(new User("geontest", "geontest@wavus.co.kr", encryptPassword, "API KEY 2"));
    }

    public Optional<User> findByEmail(String email) {
        return userList.stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    public boolean existsByEmail(String email) {
        return userList.stream().anyMatch(member -> member.getEmail().equals(email));
    }

    public Optional<User> findById(Long id) {
        return userList.stream().filter(member -> member.getUserId().equals(id)).findFirst();
    }

    public User save(User user) {
        userList.add(user);
        return user;
    }*/

    Optional<User> findByUserId(String userId);

    Boolean existsByUserId(String userId);

    User save(User user);
}
