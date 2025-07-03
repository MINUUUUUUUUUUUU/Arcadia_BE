package profit.arcadia.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.arcadia.board.Entity.Like;
import profit.arcadia.board.service.LikeService;
import profit.arcadia.user.domain.User;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.Reply;
import profit.arcadia.board.repository.BoardRepository;
import profit.arcadia.board.repository.LikeRepository;
import profit.arcadia.board.repository.ReplyRepository;
import profit.arcadia.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    @Transactional
    public void addLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        if (!boardUser.equals(loginUser)) {
            boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
        }
        board.likeChange(board.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
            .user(loginUser)
            .board(board)
            .build());
    }

    @Override
    @Transactional
    public void addLikeToReply(String email, Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User replyUser = reply.getUser();

        if (!replyUser.equals(loginUser)) {
            replyUser.likeChange(replyUser.getReceivedLikeCnt() + 1);
        }
        reply.likeChange(reply.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
            .user(loginUser)
            .reply(reply)
            .build());
    }

    @Override
    @Transactional
    public void deleteLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        if (!boardUser.equals(loginUser)) {
            boardUser.likeChange(boardUser.getReceivedLikeCnt() - 1);
        }
        board.likeChange(board.getLikeCnt() - 1);

        likeRepository.deleteByUserEmailAndBoardId(email, boardId);
    }

    @Override
    public Boolean checkLike(String email, Long boardId) {
        return likeRepository.existsByUserEmailAndBoardId(email, boardId);
    }
}