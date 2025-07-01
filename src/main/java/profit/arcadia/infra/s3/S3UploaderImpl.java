package profit.arcadia.infra.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class S3UploaderImpl implements S3Uploader {

    private final S3Client s3Client;  // AWS SDK v2 클라이언트
    private final String s3BucketName;

    @Override
    public String uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key("test/" + userId)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return "https://" + s3BucketName + "/test/" + userId;
    }

    @Override
    public byte[] downloadProfileImage(Long userId) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3BucketName)
                .key("test/" + userId)
                .build();

        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);
        return responseBytes.asByteArray();
    }
}