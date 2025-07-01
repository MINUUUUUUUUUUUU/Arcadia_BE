package profit.arcadia.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.arcadia.board.Entity.Comment;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.domain.UserRole;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.dto.CommentCreateRequest;
import profit.arcadia.board.repository.BoardRepository;
import profit.arcadia.board.repository.CommentRepository;
import profit.arcadia.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

//댓글 관련 CRUD
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void writeComment(Long boardId, CommentCreateRequest req, String email) {
        Board board = boardRepository.findById(boardId).get();
        User user = userRepository.findByEmail(email).get();
        UserRole userRole = user.getUserRole();
        board.commentChange(board.getCommentCnt() + 1);
        commentRepository.save(req.toEntity(board, user, user.getNickname()));
    }

    public List<Comment> findAll(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public Long editComment(Long commentId, String newBody, String email) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optComment.isEmpty() || optUser.isEmpty() || !optComment.get().getUser().equals(optUser.get())) {
            return null;
        }

        Comment comment = optComment.get();
        comment.update(newBody);

        return comment.getBoard().getId();
    }

    public Long deleteComment(Long commentId, String email) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optComment.isEmpty() || optUser.isEmpty() ||
                (!optComment.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.NORMAL))) {
            return null;
        }

        Board board = optComment.get().getBoard();
        board.commentChange(board.getCommentCnt() + 1);

        commentRepository.delete(optComment.get());
        return board.getId();
    }

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }
}