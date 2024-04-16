package com.employees.demo.services.impl;

import com.employees.demo.dtos.DownloadResponse;
import com.employees.demo.dtos.JasperReportConfigDto;
import com.employees.demo.services.DownloadManagerService;
import com.employees.demo.services.JasperResourceLocator;
import com.employees.demo.services.ReportExporter;
import com.employees.demo.services.ReportExporterLocator;
import com.employees.demo.utils.ContentType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static com.employees.demo.utils.ReportParamNames.REPORT_NAME_KEY;
import static com.employees.demo.utils.ReportParamNames.REPORT_PARAMS_KEY;

public class JasperDownloadManagerServiceImpl implements DownloadManagerService {

    private final DataSource dataSource;
    private final JasperResourceLocator jasperResourceLocator;
    private final ReportExporterLocator reportExporterLocator;

    public JasperDownloadManagerServiceImpl(final DataSource dataSource, final JasperResourceLocator jasperResourceLocator,
                                            final ReportExporterLocator reportExporterLocator) {
        this.dataSource = dataSource;
        this.jasperResourceLocator = jasperResourceLocator;
        this.reportExporterLocator = reportExporterLocator;
    }

    @Override
    public DownloadResponse getDownloadResponse(final Map<String, ?> request, final ContentType contentType) {
        DownloadResponse jasperResponse = null;
        try (Connection connection = dataSource.getConnection()) {
            JasperReportConfigDto jasperReportConfigDto=this.jasperResourceLocator
                    .getJasperReportConfigByName(request.get(REPORT_NAME_KEY).toString()).orElseThrow(()->
                            new IllegalArgumentException("Report not found for name :["+request.get(REPORT_NAME_KEY).toString()+"]"));
            connection.setAutoCommit(true);
            JasperPrint jasperPrint =(request.containsKey(REPORT_PARAMS_KEY))? JasperFillManager.fillReport(
                    jasperReportConfigDto.jasperReport(), (Map<String, Object>)request.get(REPORT_PARAMS_KEY), connection): JasperFillManager.fillReport(
                    jasperReportConfigDto.jasperReport(), null, connection);
            byte[] document = getReport(jasperPrint, contentType);
            jasperResponse = new DownloadResponse(jasperReportConfigDto.fileName(), contentType.getContentType(),
                    document);
        } catch (SQLException | JRException e) {
            throw new RuntimeException("Error in report generation :"+e.getMessage(),e);
        }
        return jasperResponse;
    }

    private byte[] getReport(final JasperPrint jasperPrint,final ContentType contentType)  {
        ReportExporter reportExporter=this.reportExporterLocator.getReportExporter(contentType);
        return reportExporter.exportReport(jasperPrint);
    }
}
