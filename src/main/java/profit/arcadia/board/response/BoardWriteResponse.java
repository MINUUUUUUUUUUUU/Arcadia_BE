package profit.arcadia.board.response;

import lombok.Builder;
import lombok.Data;
import profit.arcadia.user.domain.UserRole;

@Data
@Builder
public class BoardWriteResponse {
    private String message;
    private String nextUrl;
    private String isExpert;
    private UserRole userRole;
    private Integer points;
}

