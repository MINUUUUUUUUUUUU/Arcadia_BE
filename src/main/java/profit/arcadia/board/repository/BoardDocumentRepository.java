package profit.arcadia.board.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import profit.arcadia.board.dto.BoardContentDto;

@Repository
public interface BoardDocumentRepository extends MongoRepository<BoardContentDto, String> {
//    Optional<BoardContentDto> findByDocumentId(ObjectId documentId);
}
