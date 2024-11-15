package com.employees.demo.services.impl.exporters;

import com.employees.demo.services.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReportHtmlExporterImpl implements ReportExporter {
    private static final Log logger = LogFactory.getLog(ReportHtmlExporterImpl.class);

    @Override
    public byte[] exportReport(JasperPrint jasperPrint) {
        byte[] report = null;
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setConfiguration(new SimpleHtmlExporterConfiguration());
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
            exporter.exportReport();
            report = outputStream.toByteArray();
        } catch (IOException | JRException e) {
            logger.error("Error in export report :" + e.getMessage(), e);
            throw new RuntimeException("Error in export report :" + e.getMessage(), e);
        }
        return report;
    }
}
