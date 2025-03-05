package br.com.fiap.fast_food.src.configurations.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;

@Configuration
@Profile("!test")
public class AwsSecretsManagerConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.secretsmanager.endpoint}")
    private String endpoint;

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                //.region(Region.of(System.getenv("AWS_REGION")))
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint)) //só é usado por conta do teste local!!
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
