package com.employees.demo.services;

import com.employees.demo.dtos.DownloadResponse;
import com.employees.demo.utils.ContentType;

import java.util.Map;

public interface DownloadManagerService {

    DownloadResponse getDownloadResponse(Map<String, ?> request, ContentType contentType);
}
