package profit.arcadia.auth.service;

import org.springframework.transaction.annotation.Transactional;
import profit.arcadia.auth.dto.LoginUserDto;
import profit.arcadia.auth.dto.RegisterUserDto;
import profit.arcadia.user.domain.User;

public interface AuthenticationService {

    User signup(RegisterUserDto input);

    User authenticate(LoginUserDto input);

    @Transactional
    void deleteUser(String email, String password);
}
