    package profit.arcadia.board.response;


    import lombok.Builder;
    import lombok.Data;
    import profit.arcadia.board.Entity.Comment;

    import java.util.List;

    @Data
    @Builder
    public class CommentReadResponse {
        private String message;
        private List<Long> userId;
        private String nickName;
        private List<Comment> comments;
    }