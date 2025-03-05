package br.com.fiap.fast_food;

import br.com.fiap.fast_food.src.configurations.aws.AwsSecretsManagerService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class FastFoodApplicationTests {

//	@MockBean
//	private AwsSecretsManagerConfig awsSecretsManagerConfig;
//
//	@MockBean
//	private SecretLoaderConfig secretLoaderConfig;
//
//	@MockBean
//	private AwsSecretsManagerService awsSecretsManagerService;


	@Test
    void contextLoads() {
	}

}
