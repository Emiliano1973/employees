package com.employees.demo.services.impl.exporters;

import com.employees.demo.services.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ReportExcelExporterImpl implements ReportExporter {
    private static final Log logger = LogFactory.getLog(ReportExcelExporterImpl.class);

    @Override
    public byte[] exportReport(JasperPrint jasperPrint) {
        byte[] report = null;
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleXlsReportConfiguration xlsExporterConfiguration = new SimpleXlsReportConfiguration();
            xlsExporterConfiguration.setRemoveEmptySpaceBetweenRows(true);
            exporter.setConfiguration(xlsExporterConfiguration);
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
            report = outputStream.toByteArray();
        } catch (IOException | JRException e) {
            logger.error("Error in export report :" + e.getMessage(), e);
            throw new RuntimeException("Error in export report :" + e.getMessage(), e);
        }
        return report;
    }
}
