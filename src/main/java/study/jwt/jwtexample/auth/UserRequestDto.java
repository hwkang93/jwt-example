package study.jwt.jwtexample.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @ApiModelProperty(value = "사용자 ID", example = "geonedu29")
    private String userId;

    @ApiModelProperty(value = "사용자 비밀번호", example = "wavus1234!")
    private String password;

    public User toMember(PasswordEncoder passwordEncoder) {
        return User.builder()
                .password(passwordEncoder.encode(password))
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
