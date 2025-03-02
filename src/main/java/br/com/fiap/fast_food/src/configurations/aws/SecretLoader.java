package br.com.fiap.fast_food.src.configurations.aws;

import br.com.fiap.fast_food.src.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecretLoader {

    private final AwsSecretsManagerService secretsManagerService;

    @Value("${aws.secret-name}")
    private String secretName;

    @Bean
    public DataSource dataSource() {
        log.info("🔍 Buscando credenciais do banco no Secrets Manager...");

        JSONObject secret = secretsManagerService.getSecret(secretName);
        if (secret == null) {
            log.error("❌ Falha ao recuperar segredo do Secrets Manager.");
            throw new ValidationException("Secret not found.");
        }

        String dbUrl = String.format(
                "jdbc:postgresql://%s:%d/%s",
                secret.getString("host"),
                secret.getInt("port"),
                secret.getString("dbname")
        );

        log.info("✅ Conectado ao banco: {}", dbUrl);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(secret.getString("username"));
        dataSource.setPassword(secret.getString("password"));

        return dataSource;
    }
}
