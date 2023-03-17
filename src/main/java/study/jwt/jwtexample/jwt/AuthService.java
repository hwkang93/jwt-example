package study.jwt.jwtexample.jwt;

import study.jwt.jwtexample.auth.UserRequestDto;

public interface AuthService {

    TokenDto login(UserRequestDto userRequestDto);

    TokenDto reissue(TokenRequestDto tokenRequestDto);
}
