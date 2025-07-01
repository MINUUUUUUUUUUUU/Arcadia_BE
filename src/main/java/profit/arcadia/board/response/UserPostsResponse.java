package profit.arcadia.board.response;
import lombok.Builder;
import lombok.Data;
import profit.arcadia.board.Entity.Board;

import java.util.List;

@Data
@Builder
public class UserPostsResponse {
    private String message;
    private List<Board> boards;
//    private List<BoardDto> boards;  // BoardDto 리스트로 변경

}
