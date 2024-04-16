package com.employees.demo.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(final Gender attribute) {
        return attribute.getGender();
    }

    @Override
    public Gender convertToEntityAttribute(final String dbData) {
        return Gender.getGenderByString(dbData);
    }
}
