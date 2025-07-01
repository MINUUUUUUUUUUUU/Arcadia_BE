package profit.arcadia.board.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import profit.arcadia.user.domain.User;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;      // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Board board;    // 댓글이 달린 게시판

    private String nickname;


    public void update(String newBody) {
        this.body = newBody;
    }

    public Long getCommentUserId() {
        return user.getId();
    }
}