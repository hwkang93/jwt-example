package study.jwt.jwtexample.jwt;

import org.springframework.stereotype.Repository;

import java.util.*;

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
