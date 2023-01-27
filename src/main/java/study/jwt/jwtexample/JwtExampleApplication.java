package study.jwt.jwtexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
	@see : https://jangjjolkit.tistory.com/m/26

	아래가 더 좋은거 같음
	@see : https://bcp0109.tistory.com/301
 */
@SpringBootApplication(scanBasePackages = { "study.jwt" })
@ComponentScan(basePackages = {"study.jwt"})
public class JwtExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtExampleApplication.class, args);
	}

}
