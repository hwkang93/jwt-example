package study.jwt.jwtexample.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

//@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Value("${api.key}")
    private String apiKey;

    @Value("${jwt.authentication.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    public ApiAuthenticationProvider() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean isValidUser = callAuthenticationApi(name, password);

        if(isValidUser) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(apiKey);
            return new UsernamePasswordAuthenticationToken(name, password, Collections.singleton(grantedAuthority));
        }

        throw new BadCredentialsException("Invalid username or password");
    }

    private boolean callAuthenticationApi(String name, String password) throws AuthenticationServiceException {
        String requestApiUrl = new StringBuilder(apiUrl).append("?")
                .append("crtfckey=").append(apiKey)
                .append("&userId=").append(name)
                .append("&password=").append(password)
                .toString();

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(requestApiUrl, requestEntity, String.class);
            String responseBody = response.getBody();

            JsonParser jsonParser = JsonParserFactory.getJsonParser();
            Map<String, Object> responseMap = jsonParser.parseMap(responseBody);
            Object result = responseMap.get("result");

            if(result != null && result instanceof Map) {
                Map<String, String> resultMap = (Map<String, String>) result;
                String isValid = String.valueOf(resultMap.get("isValid"));
                if(isValid != null) {
                    return Boolean.valueOf(isValid);
                }
            }
            throw new BadCredentialsException("Failed to authenticate user");
        } catch (RestClientException e) {
            throw new AuthenticationServiceException("Failed to call authentication API", e);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationServiceException("Invalid response from authentication API", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
