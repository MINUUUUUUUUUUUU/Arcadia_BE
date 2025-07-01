package profit.arcadia.board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import profit.arcadia.user.domain.User;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.BoardCategory;

@Data
@Getter
@Setter
public class BoardCreateRequest {

    private String title;
    private String body;
    private Integer point = 0;
    private String documentId;

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public Board toEntity(BoardCategory category, User user) {
        return Board.builder()
                .user(user)
                .category(category)
                .title(title)
//                .body(body)
                .point(point)
                .likeCnt(0)
                .commentCnt(0)
                .documentId(documentId) // documentId를 설정하는 부분 추가
                .build();
    }

}