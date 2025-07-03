package profit.arcadia.board.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.arcadia.board.service.BoardService;
import profit.arcadia.user.domain.User;
import profit.arcadia.user.domain.UserRole;
import profit.arcadia.board.Entity.Board;
import profit.arcadia.board.Entity.BoardCategory;
import profit.arcadia.board.dto.BoardCntDto;
import profit.arcadia.board.dto.BoardContentDto;
import profit.arcadia.board.dto.BoardCreateRequest;
import profit.arcadia.board.dto.BoardDto;
import profit.arcadia.board.repository.BoardDocumentRepository;
import profit.arcadia.board.repository.BoardRepository;
import profit.arcadia.board.repository.CommentRepository;
import profit.arcadia.board.repository.LikeRepository;
import profit.arcadia.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    @Override
    public Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword) {
        if (searchType != null && keyword != null) {
            if (searchType.equals("title")) {
                return boardRepository.findAllByCategoryAndTitleContains(category, keyword, pageRequest);
            } else if (searchType.equals("author")) {
                List<User> userOptional = userRepository.findByFullNameContains(keyword);
                return boardRepository.findAllByCategoryAndUserIn(category, userOptional, pageRequest);
            }
        }
        return boardRepository.findAllByCategoryAndUserUserRoleNot(category, UserRole.NORMAL, pageRequest);
    }

    @Override
    public BoardDto getBoard(Long boardId, String category) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        if (optBoard.isEmpty()) {
            return null;
        }

        Board board = optBoard.get();
        board.incrementViewCount();
        boardRepository.save(board);

        BoardDto boardDto = BoardDto.of(board);

        Optional<BoardContentDto> savedBoardDocument = boardDocumentRepository.findById(board.getDocumentId());
        if (savedBoardDocument.isEmpty()) {
            return null;
        }

        boardDto.setBody(savedBoardDocument.get().getBody());
        return boardDto;
    }

    @Override
    public Long writeBoard(BoardCreateRequest req, BoardCategory category, String email, Authentication authentication) throws IOException {
        User loginUser = userRepository.findByEmail(email).get();
        Board savedBoard = boardRepository.save(req.toEntity(category, loginUser));

        BoardContentDto bcd = boardDocumentRepository.save(new BoardContentDto().init(req.getBody()));

        savedBoard.setDocumentId(bcd.getId());
        boardRepository.save(savedBoard);

        return savedBoard.getId();
    }

    @Override
    public Long editBoard(Long boardId, String category, BoardDto dto) throws IOException {
        Optional<Board> savedBoard = boardRepository.findById(boardId);
        if (savedBoard.isEmpty()) return null;

        Board board = savedBoard.get();
        Optional<BoardContentDto> savedBoardDocument = boardDocumentRepository.findById(board.getDocumentId());
        if (savedBoardDocument.isEmpty()) return null;

        BoardContentDto boardContentDto = savedBoardDocument.get();

        board.update(dto);
        boardContentDto.update(dto.getBody());

        boardRepository.save(board);
        boardDocumentRepository.save(boardContentDto);

        return board.getId();
    }

    @Override
    public Long deleteBoard(Long boardId, String category) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();

        boardRepository.deleteById(boardId);
        boardDocumentRepository.deleteById(board.getDocumentId());

        return boardId;
    }

    @Override
    public String getCategory(Long boardId) {
        return boardRepository.findById(boardId)
            .map(board -> board.getCategory().toString().toLowerCase())
            .orElse(null);
    }

    @Override
    public BoardCntDto getBoardCnt() {
        return BoardCntDto.builder()
            .totalBoardCnt(boardRepository.count())
            .totalNoticeCnt(boardRepository.countAllByUserUserRole(UserRole.NORMAL))
            .totalQuestionCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.QUESTION, UserRole.NORMAL))
            .totalFreeCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.FREE, UserRole.NORMAL))
            .totalInformCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.INFORM, UserRole.NORMAL))
            .totalDiaryCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.DIARY, UserRole.NORMAL))
            .build();
    }

    @Override
    public Page<Board> getPagedBoards(List<Board> boards, PageRequest pageRequest, String searchType, String keyword) {
        List<Board> filteredBoards = boards.stream()
            .filter(board -> {
                if (searchType != null && keyword != null) {
                    switch (searchType) {
                        case "title":
                            return board.getTitle().contains(keyword);
                        case "body":
                            return board.getBody().contains(keyword);
                    }
                }
                return true;
            })
            .collect(Collectors.toList());

        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filteredBoards.size());
        return new PageImpl<>(filteredBoards.subList(start, end), pageRequest, filteredBoards.size());
    }
}