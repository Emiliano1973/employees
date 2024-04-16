package com.employees.demo.services.impl.exporters;

import com.employees.demo.services.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportPdfExporterImpl implements ReportExporter {
    @Override
    public byte[] exportReport(JasperPrint jasperPrint) {
        byte[] report=null;
        try {
          report= JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Error in export report :"+e.getMessage(), e);
        }
        return report;
    }
}
