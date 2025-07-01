package profit.arcadia.board.dto;

//댓글을 입력받아 DB에 저장할 때 사용하는 DTO

import lombok.Data;
import profit.arcadia.board.Entity.Comment;
import profit.arcadia.user.domain.User;
import profit.arcadia.board.Entity.Board;

@Data
public class CommentCreateRequest {

    private String body;

    public Comment toEntity(Board board, User user, String nickname) {
        return Comment.builder()
                .user(user)
                .nickname(nickname)
                .board(board)
                .body(body)
                .build();
    }
}