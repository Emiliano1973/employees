package com.employees.demo.services.impl;

import com.employees.demo.services.ReportExporter;
import com.employees.demo.services.ReportExporterLocator;
import com.employees.demo.utils.ContentType;

import java.util.Map;

public class ReportExporterLocatorImpl implements ReportExporterLocator {

    private final Map<ContentType, ReportExporter> reportExporterMap;

    public ReportExporterLocatorImpl(final Map<ContentType, ReportExporter> reportExporterMap
                                     ) {
        this.reportExporterMap= Map.copyOf(reportExporterMap);
    }

    @Override
    public ReportExporter getReportExporter(final ContentType contentType) {
        return  this.reportExporterMap.get(contentType);
    }
}
