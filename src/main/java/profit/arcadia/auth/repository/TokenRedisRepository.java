package profit.arcadia.auth.repository;

import profit.arcadia.auth.dto.TokenRedis;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
public interface TokenRedisRepository extends CrudRepository<TokenRedis, String> {

    Optional<TokenRedis> findByAccessToken(String accessToken); // AccessToken으로 찾아내기
}
