package com.employees.demo.utils;

import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.stream.Stream;

public enum ContentType {
    PDF(MediaType.APPLICATION_PDF, "pdf"),
    EXCEL(MediaType.APPLICATION_OCTET_STREAM, "xls"),
    EXCEL_OPEN_XML(MediaType.APPLICATION_OCTET_STREAM, "xlsx"),
    HTML(MediaType.TEXT_HTML, "html");

    private final MediaType contentType;
    private final String extension;

    ContentType(MediaType contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }


    public static Optional<ContentType> getContentTypeByExtension(final  String extension){
       return Stream.of(values()).filter(ct->ct.getExtension().equalsIgnoreCase(extension)).findFirst();
    }
}
