package profit.arcadia.board.service;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.BoardCategory;
import profit.arcadia.board.dto.BoardCntDto;
import profit.arcadia.board.dto.BoardCreateRequest;
import profit.arcadia.board.dto.BoardDto;

public interface BoardService {

    Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword);

    BoardDto getBoard(Long boardId, String category);

    Long writeBoard(BoardCreateRequest req, BoardCategory category, String email, Authentication authentication) throws IOException;

    Long editBoard(Long boardId, String category, BoardDto dto) throws IOException;

    Long deleteBoard(Long boardId, String category) throws IOException;

    String getCategory(Long boardId);

    BoardCntDto getBoardCnt();

    Page<Board> getPagedBoards(List<Board> boards, PageRequest pageRequest, String searchType, String keyword);
}
