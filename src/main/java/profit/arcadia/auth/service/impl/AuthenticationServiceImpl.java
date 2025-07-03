package profit.arcadia.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.arcadia.auth.dto.LoginUserDto;
import profit.arcadia.auth.dto.RegisterUserDto;
import profit.arcadia.auth.service.AuthenticationService;
import profit.arcadia.auth.service.JwtService;
import profit.arcadia.auth.service.TokenRedisService;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRedisService tokenRedisService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setBirth(input.getBirth());
        user.setPhone(input.getPhone());
        user.setNickname(input.getNickname());
        user.setPoints(3000);
        log.info("point: {}", user.getPoints());

        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        return userRepository.findByEmail(input.getEmail())
            .orElseThrow();
    }

    @Override
    @Transactional
    public void deleteUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
    }
}