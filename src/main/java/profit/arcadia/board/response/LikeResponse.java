package profit.arcadia.board.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {
    private String message;
    private String nextUrl;
}
