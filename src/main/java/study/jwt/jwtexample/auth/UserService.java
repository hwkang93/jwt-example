package study.jwt.jwtexample.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.jwt.jwtexample.jwt.SecurityUtil;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto getMemberInfo(String email) {
        return userRepository.findByUserId(email)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    public UserResponseDto getMyInfo() {
        return userRepository.findByUserId(SecurityUtil.getCurrentMemberId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 정보가 없습니다."));
    }
}
