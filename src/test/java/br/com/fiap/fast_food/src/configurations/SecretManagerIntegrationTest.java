package br.com.fiap.fast_food.src.configurations;

import br.com.fiap.fast_food.src.configurations.aws.AwsSecretsManagerService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SecretManagerIntegrationTest {

    @Autowired
    private AwsSecretsManagerService secretsManagerService;

    @Test
    public void testGetSecretFromLocalStack() {
        String secretName = "rds-credentials";
        JSONObject secret = secretsManagerService.getSecret(secretName);

        
        assertNotNull(secret, "O segredo não foi recuperado!");
        System.out.println("✅ Segredo recuperado: " + secret.toString());
    }
}

