package profit.arcadia.board.response;


import lombok.Builder;
import lombok.Data;
import profit.arcadia.user.domain.UserRole;

@Data
@Builder
public class CommentWriteResponse {
    private String message;
    private String nextUrl;
    private String isExpert;
    private UserRole userRole;
    private Long userId;
    private String nickName;
}