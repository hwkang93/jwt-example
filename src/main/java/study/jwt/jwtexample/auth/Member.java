package study.jwt.jwtexample.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    private Long id;
    private String email;
    private String password;
    private Authority authority;
    private String apiKey;

    @Builder
    public Member(Long id, String email, String password, Authority authority, String apiKey) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }
}
