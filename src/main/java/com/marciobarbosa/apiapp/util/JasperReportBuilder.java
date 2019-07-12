package com.marciobarbosa.apiapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Component
public class JasperReportBuilder {

	public static final Logger LOGGER = LoggerFactory.getLogger(JasperReportBuilder.class);
	private static final String INLINE = "inline";
	private static final String ATTACHMENT = "attachment";

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected JasperFillManager jasperFillManager;

	@Autowired
	protected JasperExportManager jasperExportManager;

	@Autowired
	protected ContextualizedJasperFileResolver fileResolver;

	@Autowired
	protected JRXlsxExporter xlsxExporter;

	protected String destinantionFileName;

	protected JasperReport jasperReport;

	protected JasperPrint jasperPrint;

	protected List<JasperReportBuilder> concatenables = new ArrayList<>();

	protected Map<String, Object> parameters = new HashMap<>();

	public JasperReportBuilder forReport(JasperReport jasperReport) {
		this.setReport(jasperReport);
		this.jasperPrint = null;
		this.parameters = new HashMap<>();
		return this;
	}

	public JasperReportBuilder forReport(String filePath) throws JRException {
		try {
			this.setReport((JasperReport) JRLoader.loadObject(fileResolver.resolveFile("//" + filePath)));
			this.jasperPrint = null;
			this.parameters = new HashMap<>();
			return this;
		} catch (JRException e) {
			LOGGER.error("Failed to load JasperReport file at path: " + filePath, e);
			return this;
		}

	}

	public JasperReportBuilder withDestinationName(String filename) {
		this.destinantionFileName = filename;
		return this;
	}

	public void setReport(JasperReport jasperReport) {
		this.jasperReport = jasperReport;
	}

	public JasperReportBuilder withParameter(String key, Object object) {
		this.parameters.put(key, object);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <Y> Y getParamater(String key) {
		return (Y) this.parameters.get(key);
	}

	public <Y> void addParameter(String key, Y value) {
		this.parameters.put(key, value);
	}

	public void removeParameter(String key) {
		this.parameters.remove(key);
	}

	public void viewPdf() throws Exception {
		this.writeInResponseAsPdf(INLINE);
	}

	public void downloadPdf() throws Exception {
		this.writeInResponseAsPdf(ATTACHMENT);
	}

	public void downloadXls() {
		this.writeInResponseAsXls(ATTACHMENT);
	}

	public void viewBase64() {
		this.writeInResponseAsBase64();
	}

	public <Y> JasperReportBuilder fill(Collection<Y> dataList) throws JRException {

		if (jasperReport == null) {
			throw new RuntimeException("Report is not set, please set a report using the 'setReport()' method or "
					+ "create this object by the method 'forReport()'");
		}

		try {
			if (dataList.isEmpty()) {
				this.jasperPrint = jasperFillManager.fill(jasperReport, parameters, new JREmptyDataSource());
			} else {
				this.jasperPrint = jasperFillManager.fill(jasperReport, parameters,
						new JRBeanCollectionDataSource(dataList));
			}

		} catch (JRException e) {
			LOGGER.error("Fail to fill collection Data on report: " + jasperReport.getName(), e);
		}

		return this;
	}

	public <Y> void fillAndViewPdf(Collection<Y> dataList) throws Exception {

		this.<Y>fill(dataList);
		this.writeInResponseAsPdf(INLINE);

	}

	public <Y> void fillAndViewPdf() throws Exception {

		this.<Y>fill(Collections.<Y>emptyList());
		this.writeInResponseAsPdf(INLINE);

	}

	public <Y> void fillAndDownloadPdf(Collection<Y> dataList) throws Exception {

		this.<Y>fill(dataList);
		this.writeInResponseAsPdf(ATTACHMENT);

	}

	public <Y> void fillAndDownloadPdf() throws Exception {

		this.<Y>fill(Collections.<Y>emptyList());
		this.writeInResponseAsPdf(ATTACHMENT);

	}

	public <Y> void fillAndDownloadXls(Collection<Y> dataList) throws JRException {

		this.<Y>fill(dataList);
		this.writeInResponseAsXls(ATTACHMENT);

	}

	public <Y> void fillAndDownloadXls() throws JRException {

		this.<Y>fill(Collections.<Y>emptyList());
		this.writeInResponseAsXls(ATTACHMENT);

	}

	public <Y> void fillAndViewBase64(Collection<Y> dataList) throws JRException {

		this.<Y>fill(dataList);
		this.writeInResponseAsBase64();

	}

	public <Y> void fillAndViewBase64() throws JRException {

		this.<Y>fill(Collections.<Y>emptyList());
		this.writeInResponseAsBase64();

	}

	public JasperReportBuilderSnapshot snapshot() {
		return new JasperReportBuilderSnapshot(this);
	}
	
	private void writeInResponseAsBase64() {

		if (jasperPrint == null) {
			throw new RuntimeException("Report is empty, please fill it before with your data before building it.");
		}

		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {

			jasperExportManager.exportToPdfStream(jasperPrint, byteOut);

			response.getWriter().write(Base64.encodeBase64String(byteOut.toByteArray()));

			reset();

		} catch (Exception e) {
			LOGGER.error("Error in exporting Report: " + jasperReport.getName(), e);
		} finally {

		}

	}

	public JasperReportBuilder concat(JasperReportBuilder jasperBuilderSnapshot) {
		this.concatenables.add(jasperBuilderSnapshot);
		return this;
	}

	private void writeInResponseAsPdf(String writeType) throws Exception {

		if (jasperPrint == null) {
			throw new RuntimeException("Report is empty, please fill it before with your data before building it.");
		}

		try {

			String filename = destinantionFileName;

			if (filename == null) {
				filename = jasperReport.getName() + "_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date())
						+ ".pdf";
			}

			response.addHeader("Content-disposition", writeType + ";filename=" + filename);
			response.setContentType("application/pdf");

			if (!concatenables.isEmpty()) {

				PDFMergerUtility merger = new PDFMergerUtility();

				ByteArrayOutputStream mainReport = new ByteArrayOutputStream();

				jasperExportManager.exportToPdfStream(jasperPrint, mainReport);

				merger.addSource(new ByteArrayInputStream(mainReport.toByteArray()));

				for (JasperReportBuilder concatenable : concatenables) {

					ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
					jasperExportManager.exportToPdfStream(concatenable.jasperPrint, tempOut);

					merger.addSource(new ByteArrayInputStream(tempOut.toByteArray()));

					tempOut.reset();
				}

				mainReport.reset();

				merger.setDestinationStream(response.getOutputStream());

				merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

			} else {
				jasperExportManager.exportToPdfStream(jasperPrint, response.getOutputStream());
			}

			reset();

		} catch (Exception e) {
			LOGGER.error("Error in exporting Report: " + jasperReport.getName(), e);

		}

	}

	private void writeInResponseAsXls(String writeType) {
		if (jasperPrint == null) {
			throw new RuntimeException("Report is empty, please fill it before with your data before building it.");
		}

		try {

			String filename = destinantionFileName;

			if (filename == null) {
				filename = jasperReport.getName() + "_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date())
						+ ".xlsx";
			}

			response.addHeader("Content-disposition", writeType + ";filename=" + filename);
			response.setContentType("application/vnd.ms-excel");

			xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

			xlsxExporter.exportReport();

			xlsxExporter.reset();
			reset();

		} catch (Exception e) {
			LOGGER.error("Error in exporting Report: " + jasperReport.getName(), e);
			throw new RuntimeException("Error in exporting Report: " + jasperReport.getName());
		}

	}

	public void reset() {
		this.concatenables = new ArrayList<>();
		this.destinantionFileName = null;
		this.jasperPrint = null;
		this.jasperReport = null;
		this.parameters = new HashMap<>();
	}

}
