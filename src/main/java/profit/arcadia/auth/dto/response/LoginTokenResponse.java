package profit.arcadia.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginTokenResponse {
    // Getters and setters...
    // Getter 메서드
    // Setter 메서드
    private String accestoken;

    private String refreshtoken;

    private long expiresIn;
}
