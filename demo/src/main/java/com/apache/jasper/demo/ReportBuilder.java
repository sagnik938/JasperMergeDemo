package com.apache.jasper.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.xml.JasperPrintFactory;

@Service
public class ReportBuilder {

    private static final String CURRENT_PAGE_NUMBER = "${CURRENT_PAGE_NUMBER}";
    private static final String TOTAL_PAGE_NUMBER = "${TOTAL_PAGE_NUMBER}";

    public JasperPrint reportStudent(){

        try {
            String filepath = "C:/Users/SAGNIKS/Desktop/codemo/demo/src/main/resources/templates/Student.jrxml";
            String destination = "C:/Users/SAGNIKS/Desktop/codemo/demo/src/main/resources/static/";
            List<Student> studentList = new ArrayList<Student>();
            for(int i=0;i<5;i++){
                Student s = new Student(i, "abc_"+i, 50+i);
                studentList.add(s);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);
            JasperReport report = JasperCompileManager.compileReport(filepath);
            JasperPrint studentReport = JasperFillManager.fillReport(report, null, dataSource);
            JasperExportManager.exportReportToPdfFile(studentReport, destination + "STUDENT.pdf");
            return studentReport;


        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
        return null;
    }

    public JasperPrint reportEmployee(){

        try {
            String filepath = "C:/Users/SAGNIKS/Desktop/codemo/demo/src/main/resources/templates/Employee.jrxml";
            String destination = "C:/Users/SAGNIKS/Desktop/codemo/demo/src/main/resources/static/";
            List<Employee> list = new ArrayList<Employee>();
            for(int i=0;i<5;i++){
                Employee e = new Employee(i, "emp_"+i , 5000+i);
                list.add(e);
            }
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperReport report = JasperCompileManager.compileReport(filepath);
            JasperPrint employeeReport = JasperFillManager.fillReport(report, null, dataSource);
            JasperExportManager.exportReportToPdfFile(employeeReport, destination + "Employee.pdf");
            return employeeReport;

        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
        return null;
    }

    public void mergeReports(JasperPrint jp1 , JasperPrint jp2){
    String destination = "C:/Users/SAGNIKS/Desktop/codemo/demo/src/main/resources/static/";
    List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
    jasperPrints.add(jp1);
    jasperPrints.add(jp2);
    jasperPrints = this.getPageNumbers(jasperPrints);
    List pages = jasperPrints.get(1).getPages();
    for (int j = 0; j < pages.size(); j++) {
        JRPrintPage object = (JRPrintPage)pages.get(j);
        (jasperPrints.get(0)).addPage(object);
    }
    try {
        JasperExportManager.exportReportToPdfFile(jp1, destination + "MERGED.pdf");
    } catch (JRException e) {
        e.printStackTrace();
        }
    }

    public List<JasperPrint>  getPageNumbers(List<JasperPrint> jasperPrintList){

        // First loop on all reports to get totale page number
        int totPageNumber = 0;
        for (JasperPrint jp : jasperPrintList) {
            totPageNumber += jp.getPages().size();
        }

        // Second loop all reports to replace our markers with current and total number
        int currentPage = 1;
        for (JasperPrint jp : jasperPrintList) {
            List<JRPrintPage> pages = jp.getPages();
            // Loop all pages of report
            for (JRPrintPage jpp : pages) {
                List<JRPrintElement> elements = jpp.getElements();
                // Loop all elements on page
                for (JRPrintElement jpe : elements) {
                    // Check if text element
                    if (jpe instanceof JRPrintText) {
                        JRPrintText jpt = (JRPrintText) jpe;
                        // Check if current page marker
                        if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
                            jpt.setText("Page " + currentPage + " of"); // Replace marker
                            continue;
                        }
                        // Check if totale page marker
                        if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
                            jpt.setText(" " + totPageNumber); // Replace marker
                        }
                    }
                }
                currentPage++;
            }
        }
        return jasperPrintList;
    }
    
    
}
