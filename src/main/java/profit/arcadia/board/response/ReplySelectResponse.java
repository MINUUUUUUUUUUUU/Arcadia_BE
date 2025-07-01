package profit.arcadia.board.response;

import lombok.Builder;
import lombok.Data;
import profit.arcadia.user.domain.UserRole;

@Data
@Builder
public class ReplySelectResponse {
    private String message;
    private String nextUrl;
    private String isExpert;
    private UserRole userRole;
    private boolean selected;
}