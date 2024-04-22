package com.employees.demo.services.impl.exporters;

import com.employees.demo.services.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportPdfExporterImpl implements ReportExporter {

    private static final Log logger = LogFactory.getLog(ReportPdfExporterImpl.class);
    @Override
    public byte[] exportReport(JasperPrint jasperPrint) {
        byte[] report=null;
        try {
          report= JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            logger.error("Error in export report :"+e.getMessage(), e);
            throw new RuntimeException("Error in export report :"+e.getMessage(), e);
        }
        return report;
    }
}
