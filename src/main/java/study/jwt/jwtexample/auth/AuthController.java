package study.jwt.jwtexample.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import study.jwt.jwtexample.jwt.AuthService;
import study.jwt.jwtexample.jwt.TokenDto;
import study.jwt.jwtexample.jwt.TokenRequestDto;

@Api(
        tags = "권한 정보"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.login(userRequestDto));
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "비밀번호 암호화하기 (BCryptPasswordEncoder 테스트용)")
    @GetMapping("/encrypt/{password}")
    public ResponseEntity<String> encryptPassword(@PathVariable String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        return ResponseEntity.ok(encodedPassword);
    }
}
