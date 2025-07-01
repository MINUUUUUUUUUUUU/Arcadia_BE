package profit.arcadia.auth.oauth2.user;

public interface OAuth2UserUnlink {
    void unlink(String accessToken, String refreshToken);
}
