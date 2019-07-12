package com.marciobarbosa.apiapp.util;


import java.util.ArrayList;
import java.util.HashMap;

public class JasperReportBuilderSnapshot extends JasperReportBuilder{

    public JasperReportBuilderSnapshot(JasperReportBuilder builder) {

	this.response = builder.response;
	this.jasperFillManager = builder.jasperFillManager;
	this.jasperExportManager = builder.jasperExportManager;
	this.fileResolver = builder.fileResolver;
	this.xlsxExporter = builder.xlsxExporter;
	this.concatenables = new ArrayList<>(builder.concatenables);
	this.destinantionFileName = builder.destinantionFileName;
	this.jasperPrint = builder.jasperPrint;
	this.jasperReport = builder.jasperReport;
	this.parameters = new HashMap<>(builder.parameters);

    }

}
