package profit.arcadia.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import profit.arcadia.user.dto.ChangeUserDto;
import profit.arcadia.user.dto.UpdatePasswordDto;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.repository.UserRepository;
import profit.arcadia.user.service.impl.UserServiceImpl;


@RequestMapping("/users")
@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserRepository userRepository) {
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
    }

    // 유저 정보 조회 API
    @GetMapping("/read/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
        User userInfo = userServiceImpl.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody ChangeUserDto changeUserDto) {

        User updatedUser = userServiceImpl.updateUser(userId, changeUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UpdatePasswordDto updatePasswordDto, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();


        userServiceImpl.changePassword(user, updatePasswordDto.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

}
