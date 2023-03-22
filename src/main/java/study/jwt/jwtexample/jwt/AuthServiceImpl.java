package study.jwt.jwtexample.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import study.jwt.jwtexample.auth.UserRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    //Refresh Token -> 휘발성 토큰이기 때문에 Redis 같이 삭제가 편한 서버를 쓰면 도움이 된다.
    //RDBMS 의 경우 스케줄러 등을 통해 주기적으로 남은 쓰레기 데이트들을 삭제해줘야 한다.
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenDto login(UserRequestDto userRequestDto) {
        // 1. 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        Authentication authentication = authenticateUser(userRequestDto);

        // 2. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 3. RefreshToken 저장
        refreshTokenRepository.save(createRefreshToken(authentication, tokenDto.getRefreshToken()));

        // 4. 토큰 발급
        return tokenDto;
    }

    //@Transactional
    @Override
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        validateRefreshToken(tokenRequestDto.getRefreshToken());

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = findRefreshToken(authentication);

        // 4. Refresh Token 일치하는지 검사
        validateTokenMatch(tokenRequestDto.getRefreshToken(), refreshToken);

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshTokenRepository.save(refreshToken.updateValue(tokenDto.getRefreshToken()));

        // 토큰 발급
        return tokenDto;
    }

    /**
     * Login ID/PW 를 기반으로 AuthenticationToken 생성하여 유효성을 검증한다.
     *
     * @param userRequestDto
     * @return
     */
    private Authentication authenticateUser(UserRequestDto userRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = userRequestDto.toAuthentication();
        return authenticationManager.authenticate(authenticationToken);
    }

    private RefreshToken createRefreshToken(Authentication authentication, String refreshTokenValue) {
        return RefreshToken.builder()
                .key(authentication.getName())
                .value(refreshTokenValue)
                .build();
    }

    private void validateRefreshToken(String refreshTokenValue) {
        if (!tokenProvider.validateToken(refreshTokenValue)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private RefreshToken findRefreshToken(Authentication authentication) {
        return refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
    }

    private void validateTokenMatch(String requestRefreshTokenValue, RefreshToken refreshToken) {
        if (!refreshToken.getValue().equals(requestRefreshTokenValue)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}