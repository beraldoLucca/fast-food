package br.com.fiap.fast_food.src.vo;

import br.com.fiap.fast_food.src.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CpfTest {

    @Test
    void shouldCreateCpfWhenValid() {
        // Arrange
        String value = "12345678910";

        // Act
        Cpf cpf = new Cpf(value);

        // Assert
        assertEquals("123.456.789.10", cpf.getCpf());
    }

    @Test
    void shouldThrowExceptionWhenCpfIsInvalid() {
        // Arrange
        String invalidCpf = "12345";

        // Act & Assert
        assertThrows(ValidationException.class, () -> new Cpf(invalidCpf));
    }

    @Test
    void shouldThrowExceptionWhenCpfIsNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> new Cpf(null));
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        // Arrange
        Cpf cpf1 = new Cpf("12345678910");
        Cpf cpf2 = new Cpf("12345678910");
        Cpf cpf3 = new Cpf("09876543210");

        // Assert
        assertEquals(cpf1, cpf2);
        assertNotEquals(cpf1, cpf3);
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
        assertNotEquals(cpf1.hashCode(), cpf3.hashCode());
    }

    @Test
    void shouldReturnFormattedCpfInToString() {
        // Arrange
        Cpf cpf = new Cpf("98765432100");

        // Assert
        assertEquals("987.654.321.00", cpf.toString());
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        // Arrange
        Cpf cpf = new Cpf("12345678910");

        // Act
        boolean isEqual = cpf.equals(null);

        // Assert
        assertFalse(isEqual);
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentClass() {
        // Arrange
        Cpf cpf = new Cpf("12345678910");
        String other = "12345678910";

        // Act
        boolean isEqual = cpf.equals(other);

        // Assert
        assertFalse(isEqual);
    }

    @Test
    void shouldReturnTrueWhenComparedWithSameInstance() {
        // Arrange
        Cpf cpf = new Cpf("12345678910");

        // Act
        boolean isEqual = cpf.equals(cpf);

        // Assert
        assertTrue(isEqual);
    }

    @Test
    void shouldReturnTrueWhenComparedWithAnotherCpfWithSameValue() {
        // Arrange
        Cpf cpf1 = new Cpf("12345678910");
        Cpf cpf2 = new Cpf("12345678910");

        // Act
        boolean isEqual = cpf1.equals(cpf2);

        // Assert
        assertTrue(isEqual);
    }

}
