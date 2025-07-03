package profit.arcadia.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import profit.arcadia.board.response.LikeResponse;
import profit.arcadia.board.service.impl.BoardServiceImpl;
import profit.arcadia.board.service.impl.LikeServiceImpl;

@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeServiceImpl likeServiceImpl;
    private final BoardServiceImpl boardServiceImpl;

    @GetMapping("/add/{boardId}")
    public ResponseEntity<LikeResponse> addLike(@PathVariable Long boardId, Authentication auth) {
        likeServiceImpl.addLike(auth.getName(), boardId);
        String nextUrl = "/boards/" + boardServiceImpl.getCategory(boardId) + "/" + boardId;
        LikeResponse response = LikeResponse.builder()
                .message("좋아요가 추가되었습니다.")
                .nextUrl(nextUrl)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reply/add/{replyId}")
    public ResponseEntity<LikeResponse> addLikeToReply(@PathVariable Long replyId, Authentication auth) {
        likeServiceImpl.addLikeToReply(auth.getName(), replyId);
        LikeResponse response = LikeResponse.builder()
                .message("좋아요가 추가되었습니다.")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/{boardId}")
    public ResponseEntity<LikeResponse> deleteLike(@PathVariable Long boardId, Authentication auth) {
        likeServiceImpl.deleteLike(auth.getName(), boardId);
        String nextUrl = "/boards/" + boardServiceImpl.getCategory(boardId) + "/" + boardId;
        LikeResponse response = LikeResponse.builder()
                .message("좋아요가 삭제되었습니다.")
                .nextUrl(nextUrl)
                .build();
        return ResponseEntity.ok(response);
    }

}