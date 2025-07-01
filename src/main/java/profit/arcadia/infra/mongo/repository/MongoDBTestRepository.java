package profit.arcadia.infra.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import profit.arcadia.infra.mongo.dto.MongoDBTestModel;

public interface MongoDBTestRepository extends MongoRepository<MongoDBTestModel, String> {
    MongoDBTestModel findByName(String name);
}
