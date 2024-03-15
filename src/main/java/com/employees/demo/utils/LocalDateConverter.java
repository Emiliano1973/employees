package com.employees.demo.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Converter(autoApply = true)
public class LocalDateConverter  implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        if(Objects.isNull(attribute)){
            return null;
        }
        return  Date.valueOf(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        if(Objects.isNull(dbData)){
            return null;
        }
        return  dbData.toLocalDate();
    }
}
