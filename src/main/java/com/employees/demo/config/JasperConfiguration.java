package com.employees.demo.config;

import com.employees.demo.dtos.ReportConfigurationDto;
import com.employees.demo.services.DownloadManagerService;
import com.employees.demo.services.JasperResourceLocator;
import com.employees.demo.services.ReportExporter;
import com.employees.demo.services.ReportExporterLocator;
import com.employees.demo.services.impl.JasperDownloadManagerServiceImpl;
import com.employees.demo.services.impl.JasperResourceLocatorImpl;
import com.employees.demo.services.impl.ReportExporterLocatorImpl;
import com.employees.demo.services.impl.exporters.ReportExcelExporterImpl;
import com.employees.demo.services.impl.exporters.ReportExcelOpenXmlExporterImpl;
import com.employees.demo.services.impl.exporters.ReportHtmlExporterImpl;
import com.employees.demo.services.impl.exporters.ReportPdfExporterImpl;
import com.employees.demo.utils.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Configuration
public class JasperConfiguration {

    private static final Log logger = LogFactory.getLog(JasperConfiguration.class);

    private final ObjectMapper objectMapper;

    private final String configFile;

    public JasperConfiguration(final ObjectMapper objectMapper, @Value("${jasper.config.file}")  final String configFile){
        this.objectMapper=objectMapper;
        this.configFile=configFile;
    }

    @Bean
    JasperResourceLocator jasperResourceLocator(){
        ClassPathResource resource= new  ClassPathResource(this.configFile);
        if(!resource.exists()){
            throw new RuntimeException("Error, jasper config file ["+this.configFile+"] not found");
        }
        ReportConfigurationDto[]  reportConfigurations =null;
        try(final Reader theWorldStream
                    = new InputStreamReader( resource.getInputStream())) {
        reportConfigurations =this.objectMapper.readValue(theWorldStream, ReportConfigurationDto[].class);
        }catch (IOException e) {
            logger.error("Error, it cannot possible to get Jasper file config : {}", e);
            throw new RuntimeException("Error, it cannot possible to get Jasper file config : {}", e);
        }
        return new JasperResourceLocatorImpl(reportConfigurations);
    }


    @Bean
    DownloadManagerService jasperDownloadManagerService(DataSource dataSource){
        return  new JasperDownloadManagerServiceImpl(dataSource,jasperResourceLocator(), reportExporterLocator());
    }

    @Bean
    ReportExporterLocator reportExporterLocator(){
        Map<ContentType, ReportExporter> reportExporterMap=
                Map.of(ContentType.EXCEL, reportExcelExporter(),
                ContentType.EXCEL_OPEN_XML, reportExcelOpenXmlExporter(),
                ContentType.HTML, reportHtmlExporter(),
                ContentType.PDF, reportPdfExporter());
        return  new ReportExporterLocatorImpl(reportExporterMap);
    }

    @Bean( "reportExcelExporter")
    ReportExporter reportExcelExporter(){
        return new ReportExcelExporterImpl();
    }

    @Bean( "reportExcelOpenXmlExporter")
    ReportExporter reportExcelOpenXmlExporter(){
        return new ReportExcelOpenXmlExporterImpl();
    }

    @Bean( "reportHtmlExporter")
    ReportExporter reportHtmlExporter(){
        return new ReportHtmlExporterImpl();
    }

    @Bean( "reportPdfExporter")
    ReportExporter reportPdfExporter(){
        return new ReportPdfExporterImpl();
    }

}
