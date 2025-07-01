package profit.arcadia.board.response;

import lombok.Builder;
import lombok.Data;
import profit.arcadia.board.Entity.Reply;

import java.util.List;

@Data
@Builder
public class ReplyReadResponse {
    private String message;
    private List<Long> userId;
    private String nickName;
    private List<Reply> reply;
}