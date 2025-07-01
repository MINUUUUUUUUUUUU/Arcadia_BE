package profit.arcadia.board.dto;

import profit.arcadia.user.domain.User;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.BoardCategory;

public class BoardEditRequest {
    private String title;
    private String body;


    public Board toEntity(BoardCategory category, User user) {
        return Board.builder()
                .user(user)
                .category(category)
                .title(title)
                .body(body)
                .likeCnt(0)
                .commentCnt(0)
                .build();
    }
}
