package com.employees.demo.services.impl.exporters;

import com.employees.demo.services.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReportExcelOpenXmlExporterImpl implements ReportExporter {
    @Override
    public byte[] exportReport(JasperPrint jasperPrint) {
        byte[] report = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
            reportConfigXLS.setRemoveEmptySpaceBetweenRows(true);
            exporter.setConfiguration(reportConfigXLS);
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
            report = outputStream.toByteArray();
        } catch (IOException | JRException e) {
            throw new RuntimeException("Error in export report :"+e.getMessage(), e);
        }
        return report;
    }
}
