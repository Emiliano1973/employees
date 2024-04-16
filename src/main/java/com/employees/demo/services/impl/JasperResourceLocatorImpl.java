package com.employees.demo.services.impl;

import com.employees.demo.dtos.JasperReportConfigDto;
import com.employees.demo.dtos.ReportConfigurationDto;
import com.employees.demo.services.JasperResourceLocator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JasperResourceLocatorImpl implements JasperResourceLocator {

    private final Map<String, JasperReportConfigDto> jasperReportConfigMap;

    public JasperResourceLocatorImpl(ReportConfigurationDto[] jasperReportConfigArr) {
       this.jasperReportConfigMap= Stream.of(jasperReportConfigArr).map(rc->{
           ClassPathResource resource= new  ClassPathResource(rc.reportTemplatePath()+rc.reportTemplateFileName());
            if(!resource.exists()){
                throw new RuntimeException("Error, resource ["+resource.getPath()+" not found");
            }
            JasperReport jasperReport=null;
            try(InputStream theWorldStream
                        = resource.getInputStream();) {
                jasperReport
                        = JasperCompileManager.compileReport(theWorldStream);
            } catch (JRException | IOException e) {
                throw new RuntimeException(e);
            }
            return new JasperReportConfigDto(rc.reportName(), rc.reportFileName(), jasperReport);
        }).collect(Collectors.toMap(JasperReportConfigDto::name, Function.identity()));
    }

    @Override
    public Optional<JasperReportConfigDto> getJasperReportConfigByName(final String name) {
        return Optional.ofNullable(this.jasperReportConfigMap.get(name));
    }
}
