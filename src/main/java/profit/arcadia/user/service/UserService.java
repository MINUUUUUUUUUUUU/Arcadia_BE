package profit.arcadia.user.service;

import profit.arcadia.user.domain.User;
import profit.arcadia.user.dto.ChangeUserDto;

public interface UserService  {

    User getUserInfo(Long userId);

    User updateUser(Long userId, ChangeUserDto changeUserDto);

    void changePassword(User user, String newPassword);
}
