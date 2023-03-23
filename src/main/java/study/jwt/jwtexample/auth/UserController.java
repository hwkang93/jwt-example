package study.jwt.jwtexample.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "사용자 정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "email 로 조회")
    @GetMapping("/{email}")
    @ResponseBody
    public ResponseEntity<UserResponseDto> getMemberInfo(@PathVariable String email) {
        return ResponseEntity.ok(userService.getMemberInfo(email));
    }
}
