package com.employees.demo.dtos;

import net.sf.jasperreports.engine.JasperReport;

public record JasperReportConfigDto(String name,String fileName, JasperReport jasperReport) {
}
