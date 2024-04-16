package com.employees.demo.utils;

import org.springframework.core.convert.converter.Converter;

public class ContentTypeConverter implements Converter<String, ContentType> {
    @Override
    public ContentType convert(String source) {
        return ContentType.getContentTypeByExtension(source)
                .orElseThrow(()->new IllegalArgumentException("Error parameter in input with value ["
                        +source+"] is not a valid content type"));
    }
}
