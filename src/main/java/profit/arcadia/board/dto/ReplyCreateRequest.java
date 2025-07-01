package profit.arcadia.board.dto;

//댓글을 입력받아 DB에 저장할 때 사용하는 DTO

import lombok.Data;
import profit.arcadia.user.domain.User;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.Reply;

@Data
public class ReplyCreateRequest {

    private String body;

    public Reply toEntity(Board board, User user, String nickname) {
        return Reply.builder()
                .user(user)
                .nickname(nickname)
                .board(board)
                .likeCnt(0)
//                .body(body)
                .build();
    }
}