package study.jwt.jwtexample.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Value("${api.key}")
    private String apiKey;

    @Value("${jwt.authentication.api.url}")
    private String apiUrl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        apiUrl += "?crtfckey="+apiKey;
        apiUrl += "&userId=" + name;
        apiUrl += "&password="+password;

        boolean callApiResult = false;
        String callApiResultMessage = "";

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            String responseBody = response.getBody();

            JsonParser jsonParser = JsonParserFactory.getJsonParser();
            Map<String, Object> responseMap = jsonParser.parseMap(responseBody);
            Object result = responseMap.get("result");

            if(result != null) {
                Map<String, String> resultMap = (Map<String, String>) result;
                callApiResult =Boolean.valueOf(String.valueOf(resultMap.get("isValid")));
                callApiResultMessage = resultMap.get("message");
            }
        } catch (Exception e) {
            callApiResultMessage = e.getMessage();
        }

        if(callApiResult) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(apiKey);
            return new UsernamePasswordAuthenticationToken(name, password, Collections.singleton(grantedAuthority));
        }

        throw new BadCredentialsException(callApiResultMessage);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
