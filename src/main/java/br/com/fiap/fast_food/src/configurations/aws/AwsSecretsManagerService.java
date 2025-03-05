package br.com.fiap.fast_food.src.configurations.aws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import org.json.JSONObject;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!test")
public class AwsSecretsManagerService {

    private final SecretsManagerClient secretsManagerClient;

    public JSONObject getSecret(String secretName) {
        log.info("🔍 Buscando segredo: {}", secretName);

        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
        return new JSONObject(response.secretString());
    }
}
