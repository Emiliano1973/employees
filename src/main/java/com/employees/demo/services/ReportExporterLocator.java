package com.employees.demo.services;


import com.employees.demo.utils.ContentType;

public interface ReportExporterLocator {

    ReportExporter getReportExporter(ContentType contentType);
}
