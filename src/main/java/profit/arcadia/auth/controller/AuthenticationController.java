package profit.arcadia.auth.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import profit.arcadia.auth.dto.EmailCheckReq;
import profit.arcadia.auth.dto.LoginUserDto;
import profit.arcadia.auth.dto.RegisterUserDto;
import profit.arcadia.user.domain.User;
import profit.arcadia.auth.dto.response.LoginTokenResponse;
import profit.arcadia.auth.service.impl.AuthenticationServiceImpl;
import profit.arcadia.notification.service.impl.EmailServiceImpl;
import profit.arcadia.auth.service.JwtService;
import profit.arcadia.auth.service.TokenRedisService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthenticationController {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRedisService tokenRedisService;

    private final AuthenticationServiceImpl authenticationServiceImpl;

    private final EmailServiceImpl emailServiceImpl;

    public AuthenticationController(JwtService jwtService, AuthenticationServiceImpl authenticationServiceImpl, UserDetailsService userDetailsService, TokenRedisService tokenRedisService, EmailServiceImpl emailServiceImpl) {
        this.jwtService = jwtService;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.userDetailsService = userDetailsService;
        this.tokenRedisService = tokenRedisService;
        this.emailServiceImpl = emailServiceImpl;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationServiceImpl.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }



    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        User authenticatedUser = authenticationServiceImpl.authenticate(loginUserDto);

        String accessToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        // refresh token을 Redis에 저장
        tokenRedisService.saveToken(String.valueOf(authenticatedUser.getId()), refreshToken);

        // access token 쿠키 생성 및 설정
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // HTTPS 사용 시에만 설정
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) jwtService.getExpirationTime()); // 만료 시간 설정

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);

        LoginTokenResponse loginResponse = new LoginTokenResponse();
        loginResponse.setAccestoken(accessToken);
        loginResponse.setRefreshtoken(refreshToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }


    @DeleteMapping("/logout/{id}")
    public ResponseEntity<Map<String, String>> logout(@PathVariable String id) {
        tokenRedisService.deleteRefreshToken(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }



    @PostMapping("/refresh/{id}")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> refreshTokenRequest, @PathVariable Integer id) {
        String refreshToken = refreshTokenRequest.get("refreshToken");

        // RefreshToken을 사용하여 UserDetails를 가져옵니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));

        // 새로운 AccessToken과 RefreshToken을 생성합니다.
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        tokenRedisService.saveToken(String.valueOf(id), newRefreshToken);
        String NewSavedRefreshToken = tokenRedisService.getRefreshToken(String.valueOf(id));

        // 새로운 AccessToken과 RefreshToken을 클라이언트에게 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token/{id}")
    public ResponseEntity<String> getRefreshToken(@PathVariable String id) {
        String refreshToken = tokenRedisService.getRefreshToken(id);
        if (refreshToken != null) {
            return ResponseEntity.ok(refreshToken);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //인증메일 발송
    @PostMapping("/sign-up/emailCheck")
    public ResponseEntity<Map<String, String>> emailCheck(@RequestBody EmailCheckReq emailCheckReq) throws MessagingException, UnsupportedEncodingException {
        log.info(emailCheckReq.getEmail());
        String authCode = emailServiceImpl.sendEmail(emailCheckReq.getEmail());
        String email = emailCheckReq.getEmail();
        log.info("getEmail: " + email);
        log.info("authCode: " + authCode);
        // refresh token과 access token을 Redis에 저장
        tokenRedisService.saveCode(email, authCode);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("authCode", authCode);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/sign-up/verify")
    public ResponseEntity<Map<String, String>> emailVerify(@RequestBody EmailCheckReq emailCheckReq) throws MessagingException, UnsupportedEncodingException{
        String emailCode  = tokenRedisService.getCode(emailCheckReq.getEmail());

        if(emailCode.equals(emailCheckReq.getEmailcode())){
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("emailCode: ", emailCode);
            log.info("인증되었습니다");
            return ResponseEntity.ok(responseBody);
        }
        else {
            log.info("이메일이 일치하지 않습니다.");
            return ResponseEntity.notFound().build();

        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody RegisterUserDto registerUserDto) {
        String email = registerUserDto.getEmail();
        log.info("Email: " + email);
        String password = registerUserDto.getPassword();
        log.info("password: " + password);
        authenticationServiceImpl.deleteUser(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }





}