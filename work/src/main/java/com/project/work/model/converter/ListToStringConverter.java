package com.project.work.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    private static final String DELIMITER = ",";
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if( attribute == null || attribute.isEmpty() )
            return null;

        return String.join(DELIMITER, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if( dbData == null || dbData.isEmpty() )
            return new ArrayList<>();

        return new ArrayList<>(Arrays.asList(dbData.split(DELIMITER)));
    }
}
