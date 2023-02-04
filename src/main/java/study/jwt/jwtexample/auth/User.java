package study.jwt.jwtexample.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String apiKey;

    @Builder
    public User(Long id, String email, String password, String apiKey) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }
}
