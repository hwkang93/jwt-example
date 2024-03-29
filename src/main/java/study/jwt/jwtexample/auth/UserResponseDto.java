package study.jwt.jwtexample.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String userId;
    private String apiKey;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getUserId(), null);
    }
}
