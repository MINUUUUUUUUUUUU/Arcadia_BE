// ReployController.java
package profit.arcadia.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.domain.UserRole;
import profit.arcadia.board.Entity.Reply;
import profit.arcadia.board.dto.ReplyCreateRequest;
import profit.arcadia.board.repository.ReplyRepository;
import profit.arcadia.board.response.ReplyReadResponse;
import profit.arcadia.board.response.ReplySelectResponse;
import profit.arcadia.board.response.ReplyWriteResponse;
import profit.arcadia.board.service.BoardService;
import profit.arcadia.board.service.CommentService;
import profit.arcadia.board.service.ReplyService;
import profit.arcadia.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@RestController
@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final ReplyService replyService;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;


    // 답변 작성
    @PostMapping("/write/{boardId}")
    public ResponseEntity<ReplyWriteResponse> commentWrite(@PathVariable Long boardId, @RequestBody ReplyCreateRequest req,
                                                           Authentication authentication) throws IOException {

        // 댓글 작성 서비스 호출
        replyService.writeReply(boardId, req, authentication.getName());

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        UserRole userRole = user.getUserRole();

        String nickName = user.getNickname();

        String isExpert;

        if (userRole.equals((UserRole.EXPERT))){
            isExpert = "전문가입니다.";
        } else{
            isExpert = "일반유저입니다";
        }

        // 응답 메시지와 다음 URL 설정
        String message = "답글이 추가되었습니다.";
        String nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;

        // 응답 객체 생성
        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .isExpert(isExpert)
                .userRole(userRole)
                .nickName(nickName)
                .build();

    // // ResponseEntity로 응답 반환
     return ResponseEntity.ok(response);
     }

     // 답변 수정

    @PostMapping("/{replyId}/edit")
    public ResponseEntity<ReplyWriteResponse> editReply(@PathVariable Long replyId, @RequestBody ReplyCreateRequest req,
                                                            Authentication authentication) {
        Long boardId = replyService.editReply(replyId, req.getBody(), authentication.getName());

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "잘못된 요청입니다.";
            nextUrl = "/";
        } else {
            message = "답글이 수정 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

     return ResponseEntity.ok(response);
     }


     // 답변 삭제
    @GetMapping("/{replyId}/delete")
    public ResponseEntity<ReplyWriteResponse> deleteComment(@PathVariable Long replyId, Authentication authentication) {
        Long boardId = replyService.deleteReply(replyId, authentication.getName());



        String message;
        String nextUrl;
        if (boardId == null) {
            message = "작성자만 삭제 가능합니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 삭제 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping ("/{boardId}/read")
    public ResponseEntity<ReplyReadResponse> getReplyByBoardId(@PathVariable Long boardId, Authentication authentication){
        List<Reply> reply = replyService.getReplyByBoardId(boardId);

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();

        String nickName = user.getNickname();

        String message;
        if (reply == null){
            message =  "답글이 없습니다.";
        }
        else{
            message = "답글을 불러왔습니다.";
        }

        ReplyReadResponse response = ReplyReadResponse.builder()
                .message(message)
                .nickName(nickName)
                .reply(reply)
                .build();

        return ResponseEntity.ok(response);


    }

    // 답변채택

    @PostMapping("/select/{replyId}")
    public ResponseEntity<ReplySelectResponse> selectReply(@PathVariable Long replyId, Authentication authentication) {

        Reply reply = replyRepository.findById(replyId).get();

        if(reply.isSelected() == false){
            replyService.selectReply(replyId, authentication.getName());
            boolean selected = true;

            ReplySelectResponse response = ReplySelectResponse.builder()
                    .message("답변이 채택되었습니다.")
                    .nextUrl("/boards/" + boardService.getCategory(replyId) + "/" + replyId)
                    .selected(selected)
                    .build();

            return ResponseEntity.ok(response);
        }
        else{
            boolean selected = false;
            ReplySelectResponse response = ReplySelectResponse.builder()
                    .message("이미 채택되었습니다.")
                    .selected(selected)
                    .nextUrl("/boards/" + boardService.getCategory(replyId) + "/" + replyId)
                    .build();

            return ResponseEntity.ok(response);
        }





    }


}