package study.jwt.jwtexample.jwt;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {
    Map<String, RefreshToken> memoryRefreshTokenRepository = new HashMap<>();

    public Optional<RefreshToken> findByKey(String key) {
        return Optional.of(new RefreshToken("test", "testBody"));
    }

    public void save() {

    }
}
