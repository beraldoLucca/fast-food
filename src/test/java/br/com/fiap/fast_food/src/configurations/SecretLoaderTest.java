package br.com.fiap.fast_food.src.configurations;

import br.com.fiap.fast_food.src.configurations.aws.AwsSecretsManagerService;
import br.com.fiap.fast_food.src.configurations.aws.SecretLoader;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecretLoaderTest {

    @Mock
    private AwsSecretsManagerService secretsManagerService;

    private SecretLoader secretLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        secretLoader = new SecretLoader(secretsManagerService, "rds-credentials");
    }

    @Test
    void shouldReturnDataSourceWhenSecretIsValid() throws JSONException {
        // Arrange
        JSONObject secretMock = new JSONObject()
                .put("host", "localhost")
                .put("port", 5432)
                .put("dbname", "testdb")
                .put("username", "admin")
                .put("password", "1234");

        when(secretsManagerService.getSecret("rds-credentials")).thenReturn(secretMock);

        // Act
        DataSource dataSource = secretLoader.dataSource();

        // Assert
        assertNotNull(dataSource);
        assertInstanceOf(DriverManagerDataSource.class, dataSource);
        DriverManagerDataSource ds = (DriverManagerDataSource) dataSource;
        assertEquals("jdbc:postgresql://localhost:5432/testdb", ds.getUrl());
        assertEquals("admin", ds.getUsername());
        assertEquals("1234", ds.getPassword());

        verify(secretsManagerService, times(1)).getSecret("rds-credentials");
    }

    @Test
    void sholdThrowValidationExceptionWhenSecretIsNull() {
        // Arrange
        when(secretsManagerService.getSecret("rds-credentials")).thenReturn(null);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> secretLoader.dataSource());
        assertEquals("Secret not found.", exception.getMessage());

        verify(secretsManagerService, times(1)).getSecret("rds-credentials");
    }

    @Test
    void shouldThrowValidationExceptionWhenMissEssentialFields() throws JSONException {
        // Arrange
        JSONObject secretInvalido = new JSONObject().put("host", "localhost"); // Faltam port, dbname, username, password

        when(secretsManagerService.getSecret("rds-credentials")).thenReturn(secretInvalido);

        // Act & Assert
        assertThrows(Exception.class, () -> secretLoader.dataSource());

        verify(secretsManagerService, times(1)).getSecret("rds-credentials");
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SecretLoader secretLoader(AwsSecretsManagerService secretsManagerService) {
            return new SecretLoader(secretsManagerService, "rds-credentials"); // Simula injeção do @Value
        }
    }
}
