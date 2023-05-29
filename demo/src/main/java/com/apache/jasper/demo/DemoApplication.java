package com.apache.jasper.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.sf.jasperreports.engine.JasperPrint;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		ReportBuilder reportBuilder = new ReportBuilder();
		JasperPrint jp1 = reportBuilder.reportStudent();
		JasperPrint jp2 = reportBuilder.reportEmployee();

		reportBuilder.mergeReports(jp1, jp2);
	}

}
