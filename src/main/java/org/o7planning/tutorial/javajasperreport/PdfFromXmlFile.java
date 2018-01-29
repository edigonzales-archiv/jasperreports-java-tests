package org.o7planning.tutorial.javajasperreport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.CsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.WriterExporterOutput;

public class PdfFromXmlFile {

	public static void main(String[] args) throws JRException, SQLException {

		// Get database connection.
		String url = "jdbc:postgresql://192.168.50.5/sogis";
		Properties props = new Properties();
		props.setProperty("user","ddluser");
		props.setProperty("password","ddluser");
		props.setProperty("ssl","false");
		Connection conn = DriverManager.getConnection(url, props);


		// Compile jrxml file.
		JasperReport jasperReport = JasperCompileManager.compileReport("Example2.jrxml");

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();

		// Create JasperPrinter
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

		// Export to PDF & HTML.
		JasperExportManager.exportReportToPdfFile(jasperPrint, "Example2.pdf");
		JasperExportManager.exportReportToHtmlFile(jasperPrint, "Example2.html");
		
		// Export to XLSX.
        JRXlsExporter xlsExporter = new JRXlsExporter();
        
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("Example2.xlsx"));
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(false);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        xlsReportConfiguration.setDetectCellType(false);
        xlsReportConfiguration.setWhitePageBackground(false);
        xlsExporter.setConfiguration(xlsReportConfiguration);

        xlsExporter.exportReport();

		// Export to CSV.
        JRCsvExporter csvExporter = new JRCsvExporter();
        
        csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        csvExporter.setExporterOutput(new SimpleWriterExporterOutput("Example2.csv"));
        SimpleCsvExporterConfiguration simpleCsvExporterConfiguration = new SimpleCsvExporterConfiguration();
        simpleCsvExporterConfiguration.setFieldEnclosure("\n");
        simpleCsvExporterConfiguration.setForceFieldEnclosure(true);
//        simpleCsvExporterConfiguration.setFieldDelimiter("'");
//        simpleCsvExporterConfiguration.setRecordDelimiter(";");
        csvExporter.setConfiguration(simpleCsvExporterConfiguration);
        
        csvExporter.exportReport();
        
		System.out.println("Done!");

	}

}
