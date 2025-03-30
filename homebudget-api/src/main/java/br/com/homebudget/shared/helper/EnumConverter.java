package br.com.homebudget.shared.helper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

public abstract class EnumConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

    private final Class<T> enumClass;

    protected EnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T enumValue) {
        if (enumValue == null) {
            return null;
        }
        try {
            return (String) enumClass.getMethod("getDescricao").invoke(enumValue);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao converter enum para banco de dados", e);
        }
    }

    @Override
    public T convertToEntityAttribute(String descricao) {
        if (descricao == null) {
            return null;
        }

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> {
                    try {
                        return ((String) enumClass.getMethod("getDescricao").invoke(e)).equals(descricao);
                    } catch (Exception ex) {
                        throw new IllegalStateException("Erro ao converter descrição para enum", ex);
                    }
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Valor desconhecido: " + descricao));
    }
}

