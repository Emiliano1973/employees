package com.employees.demo.services;

import com.employees.demo.dtos.JasperReportConfigDto;

import java.util.Optional;

public interface JasperResourceLocator {


    Optional<JasperReportConfigDto> getJasperReportConfigByName(String name);
}
