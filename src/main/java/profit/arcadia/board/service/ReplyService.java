package profit.arcadia.board.service;

import java.util.List;
import profit.arcadia.board.Entity.Reply;
import profit.arcadia.board.dto.ReplyCreateRequest;

public interface ReplyService {

    void writeReply(Long boardId, ReplyCreateRequest req, String email);

    List<Reply> findAll(Long boardId);

    Long editReply(Long replyId, String newBody, String email);

    Long deleteReply(Long replyId, String email);

    List<Reply> getReplyByBoardId(Long boardId);

    void selectReply(Long replyId, String email);
}
