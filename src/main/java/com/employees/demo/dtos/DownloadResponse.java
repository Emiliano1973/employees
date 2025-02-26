package com.employees.demo.dtos;

import lombok.Getter;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class DownloadResponse {

    @Getter
    private final String fileName;
    @Getter
    private final MediaType contentType;
    @Getter
    private final int responseLength;
    private final byte[] response;

    public DownloadResponse(final String fileName, final MediaType contentType, final byte[] response) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.responseLength = response.length;
        byte[] internalRes = new byte[this.responseLength];
        System.arraycopy(response, 0, internalRes, 0, this.responseLength);
        this.response = internalRes;
    }

    public InputStream getResponseStream() {
        return new ByteArrayInputStream(this.response, 0, this.responseLength);
    }
}
