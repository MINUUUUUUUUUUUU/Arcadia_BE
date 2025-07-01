package profit.arcadia.infra.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Uploader {
    String uploadProfileImage(Long userId, MultipartFile file) throws IOException;
    byte[] downloadProfileImage(Long userId);
}