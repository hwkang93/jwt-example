package study.jwt.jwtexample.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "cmmn_user_info")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "user_password_encpt")
    private String password;

    @Column(name = "user_sttus_code")
    private String userSttusCode;

    @Builder
    public User(String userId, String userNm, String password, String userSttusCode) {
        this.userId = userId;
        this.userNm = userNm;
        this.password = password;
        this.userSttusCode = userSttusCode;
    }
}
