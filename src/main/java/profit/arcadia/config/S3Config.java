package profit.arcadia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import java.net.URI;

@Configuration
public class S3Config {
    @Value("${aws.s3.endpoint:}")
    private String s3Endpoint;
    @Value("${aws.s3.accessKey:}")
    private String s3AccessKey;
    @Value("${aws.s3.secretKey:}")
    private String s3SecretKey;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(s3Endpoint))  // MinIO 주소
                .region(Region.US_EAST_1)  // 아무거나, MinIO는 검증 안함
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3AccessKey, s3SecretKey)
                ))
                .build();
    }

    @Bean
    public String s3BucketName() {
        return bucketName;  // 주입할 Bucket 이름 Bean 등록
    }
}
