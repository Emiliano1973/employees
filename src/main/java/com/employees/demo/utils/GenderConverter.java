package com.employees.demo.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.getSex();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return Gender.getGenderByString(dbData);
    }
}
