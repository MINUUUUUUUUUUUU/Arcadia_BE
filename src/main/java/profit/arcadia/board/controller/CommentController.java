// CommentController.java
package profit.arcadia.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.domain.UserRole;
import profit.arcadia.board.Entity.Comment;
import profit.arcadia.board.dto.CommentCreateRequest;
import profit.arcadia.board.repository.CommentRepository;
import profit.arcadia.board.response.CommentReadResponse;
import profit.arcadia.board.response.CommentWriteResponse;
import profit.arcadia.board.service.impl.BoardServiceImpl;
import profit.arcadia.board.service.impl.CommentServiceImpl;
import profit.arcadia.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@RestController
@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;
    private final BoardServiceImpl boardServiceImpl;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/write/{boardId}")
    public ResponseEntity<CommentWriteResponse> commentWrite(@PathVariable Long boardId,
            @RequestBody CommentCreateRequest req,
            Authentication authentication) throws IOException {

        // 댓글 작성 서비스 호출
        commentServiceImpl.writeComment(boardId, req, authentication.getName());

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();

        String nickName = user.getNickname();

        Long userId = user.getId();
        UserRole userRole = user.getUserRole();

        String isExpert;

        if (userRole.equals((UserRole.EXPERT))){
            isExpert = "전문가입니다.";
        } else{
            isExpert = "일반유저입니다";
        }

        // 응답 메시지와 다음 URL 설정
        String message = "댓글이 추가되었습니다.";
        String nextUrl = "/boards/" + boardServiceImpl.getCategory(boardId) + "/" + boardId;

        // 응답 객체 생성
        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .isExpert(isExpert)
                .userRole(userRole)
                .userId(userId)
                .nickName(nickName)
                .build();

        // ResponseEntity로 응답 반환
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}/edit")
    public ResponseEntity<CommentWriteResponse> editComment(@PathVariable Long commentId,
            @RequestBody CommentCreateRequest req,
            Authentication authentication) {
        Long boardId = commentServiceImpl.editComment(commentId, req.getBody(), authentication.getName());

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();

        String nickName = user.getNickname();

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "게시물의 ID가 비어있습니다. 잘못된 요청입니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 수정 되었습니다.";
            nextUrl = "/boards/" + boardServiceImpl.getCategory(boardId) + "/" + boardId;
        }

        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .nickName(nickName)
                .userId(user.getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commentId}/delete")
    public ResponseEntity<CommentWriteResponse> deleteComment(@PathVariable Long commentId,
            Authentication authentication) {
        Long boardId = commentServiceImpl.deleteComment(commentId, authentication.getName());

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "작성자만 삭제 가능합니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 삭제 되었습니다.";
            nextUrl = "/boards/" + boardServiceImpl.getCategory(boardId) + "/" + boardId;
        }

        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{boardId}/read")
    public ResponseEntity<CommentReadResponse> getCommentsByBoardId(@PathVariable Long boardId, Authentication authentication) {
        List<Comment> comments = commentServiceImpl.getCommentsByBoardId(boardId);

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();

        String nickName = user.getNickname();

        String message;
        if (comments == null){
            message =  "댓글이 없습니다.";
        }
        else{
            message = "댓글을 불러왔습니다.";
        }

//        List<Long> userIds = comments.stream()
//                .map(comment -> comment.getUser().getId())
//                .collect(Collectors.toList());



        CommentReadResponse response = CommentReadResponse.builder()
                .message(message)
                .nickName(nickName)
                .comments(comments)
                .build();

        return ResponseEntity.ok(response);
    }
}