package study.jwt.jwtexample.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.jwt.jwtexample.jwt.provider.api.ApiAuthenticationProvider;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * JWT Token 발행 / 검증 클래스
     */
    private final TokenProvider tokenProvider;

    /**
     * 401 처리
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 403 처리
     */
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Value("${jwt.available}")
    private boolean jwtAvailable;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",

            "/csrf",
            //"/auth",
            "/auth/**",
            "/",
            "/favicon.ico"
    };

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(apiAuthenticationProvider());
    }

    //AuthenticationManagerBuilder 를 빌드하여 AuthenticationManager 를 Bean 객체로 만든다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 로그인 사용자 인증 클래스
     */
    @Bean
    public ApiAuthenticationProvider apiAuthenticationProvider() {
        return new ApiAuthenticationProvider();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(jwtAvailable) {
            // CSRF 설정 Disable
            http.csrf().disable()

                    // exception handling
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)

                    .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()

                    // 세션을 사용하지 않음 (Spring Security 기본값 : 세션)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    // PERMIT_URL_ARRAY 에 담긴 URL 은 권한 확인을 하지 않는다.
                    .and()
                    .authorizeRequests(authorize -> authorize
                            .antMatchers(PERMIT_URL_ARRAY).permitAll().anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.csrf().disable()
                .authorizeRequests().anyRequest().permitAll();
        }
    }

}
