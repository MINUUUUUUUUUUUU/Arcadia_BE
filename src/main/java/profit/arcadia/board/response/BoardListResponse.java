package profit.arcadia.board.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.dto.BoardSearchRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponse {
    private String category;
    private Page<Board> boards;
    private BoardSearchRequest boardSearchRequest;
}