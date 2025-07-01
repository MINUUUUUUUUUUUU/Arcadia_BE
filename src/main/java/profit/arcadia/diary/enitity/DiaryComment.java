/*
package profit.arcadia.diary.enitity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import profit.arcadia.entity.User;
import profit.arcadia.board.Entity.BaseEntity;
import profit.arcadia.board.Entity.Board;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DiaryComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;    // 댓글이 달린 게시판

    public void update(String newBody) {
        this.body = newBody;
    }
}*/
