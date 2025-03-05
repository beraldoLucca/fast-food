package br.com.fiap.fast_food.src.configurations.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class SecretLoaderConfig {

    @Bean
    public SecretLoader secretLoader(AwsSecretsManagerService secretsManagerService) {
        String secretName = System.getenv("SECRET_NAME");
        if (secretName == null || secretName.isBlank()) {
            secretName = "rds-credentials"; // Default para desenvolvimento
        }
        return new SecretLoader(secretsManagerService, secretName);
    }

    @Bean
    public DataSource dataSource(SecretLoader secretLoader) {
        return secretLoader.dataSource();
    }
}