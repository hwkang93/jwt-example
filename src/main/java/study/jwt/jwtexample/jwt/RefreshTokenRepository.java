package study.jwt.jwtexample.jwt;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Refresh Token 은, REDIS SESSION 같은 외부 저장소를 이용하는 것이 좋다.
 */
@Repository
public class RefreshTokenRepository {
    List<RefreshToken> refreshTokenList = new ArrayList<>();

    public Optional<RefreshToken> findByKey(String key) {
        return refreshTokenList.stream().filter(refreshToken -> refreshToken.getKey().equals(key)).findFirst();
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenList.add(refreshToken);
    }
}
