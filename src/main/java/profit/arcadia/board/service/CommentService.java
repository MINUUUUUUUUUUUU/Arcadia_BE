package profit.arcadia.board.service;

import java.util.List;
import profit.arcadia.board.Entity.Comment;
import profit.arcadia.board.dto.CommentCreateRequest;

public interface CommentService {

    void writeComment(Long boardId, CommentCreateRequest req, String email);

    List<Comment> findAll(Long boardId);

    Long editComment(Long commentId, String newBody, String email);

    Long deleteComment(Long commentId, String email);

    List<Comment> getCommentsByBoardId(Long boardId);
}
