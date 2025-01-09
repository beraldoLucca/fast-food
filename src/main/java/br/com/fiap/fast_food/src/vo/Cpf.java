package br.com.fiap.fast_food.src.vo;

import br.com.fiap.fast_food.src.exceptions.ValidationException;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Cpf {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private String cpf;

    protected Cpf(){}

    public Cpf(String value) {
        if(!isValid(value)){
            throw new ValidationException("CPF invalido");
        }
        this.cpf = value;
    }

    private boolean isValid(String value) {
        return value != null && CPF_PATTERN.matcher(value).matches();
    }

    public String getCpf() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "." + cpf.substring(9, 11);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpfObject = (Cpf) o;
        return Objects.equals(cpf, cpfObject.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return getCpf();
    }
}
