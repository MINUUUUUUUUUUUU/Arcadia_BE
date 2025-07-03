package profit.arcadia.board.service;

public interface LikeService {

    void addLike(String email, Long boardId);

    void addLikeToReply(String email, Long replyId);

    void deleteLike(String email, Long boardId);

    Boolean checkLike(String email, Long boardId);
}