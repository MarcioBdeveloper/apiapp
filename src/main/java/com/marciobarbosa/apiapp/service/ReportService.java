package com.marciobarbosa.apiapp.service;

import java.awt.Color;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marciobarbosa.apiapp.dto.ColunmAgrupamentoRelDTO;
import com.marciobarbosa.apiapp.dto.GrupoRelDTO;
import com.marciobarbosa.apiapp.dto.ReceitaDespesaMuniRelDTO;
import com.marciobarbosa.apiapp.dto.ReceitaLiquidaRelDTO;
import com.marciobarbosa.apiapp.dto.SubGrupoRelDTO;
import com.marciobarbosa.apiapp.util.ContextualizedJasperFileResolver;
import com.marciobarbosa.apiapp.util.JRElementConfig;
import com.marciobarbosa.apiapp.util.JasperReportBuilder;
import com.marciobarbosa.apiapp.util.JasperReportBuilderSnapshot;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 10/07/2019
 *
 * */

@Service
public class ReportService {
	
    private static final String TEMPLETE = "\\fluxo-caixa-detalhado-gerencial.jrxml";
	private static final String REALIZADO2 = "REALIZADO";
	private static final String PROJETADO2 = "Projetado";
	private static final String REALIZADO = "Realizado";
	private static final String RESUMO = "RESUMO";
	private static final String PROJETADO = "PROJETADO";
	private static final String TODOS = "TODOS";
	private final static int COLUMN_HEADER_HEIGHT = 40;
    private final static String SANS_SERIF_FONT = "SansSerif";
	private JasperDesign relatorioPrincipal;
	private JRDesignBand columnHeader;
	private JRDesignRectangle colunasCard;
	private JRDesignRectangle colunasResumoCard;
	private JasperDesign columnHeaderTemplete;
	
	//cards
	private int widthColumn;
	private int xInitialHeader;
	private int widthColumnResumo;
	private int xInitialHeaderResumo;
	
	private final static int yDefaultElementoColumn1 = 0;
	private final static int yDefaultElementoColumn2 = 15;
	
	private String tipo;
    
	@Autowired
    private ContextualizedJasperFileResolver fileResolver;
	
	@Autowired
    private JasperReportBuilder jasperBuilder;
	
	public void montarRelatorio() throws JRException {
		
		Map<String, JasperReport> subreports = new HashMap<>();
    	
		//TODO ajustar para passar a quantidade de colunas de acordo com o filtro
    	JasperReportBuilderSnapshot resultado = jasperBuilder.forReport(
    			JasperCompileManager.compileReport(montarReport(2, TODOS)))
    			.withParameter("mesInicio", LocalDate.now().toString())
    			.withParameter("mesFim", LocalDate.now().toString())
    			.fill(montarDados()).snapshot();
    	
    	try {
    		resultado.downloadPdf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JasperDesign montarReport(int qtdCol, String tipo) throws JRException {
		
		this.tipo = tipo;
		
		this.relatorioPrincipal = JRXmlLoader.load(fileResolver.resolveFile(TEMPLETE));
    	this.columnHeader = (JRDesignBand) this.relatorioPrincipal.getColumnHeader();
    	this.colunasCard = (JRDesignRectangle) this.columnHeader.getElementByKey("colunasCard");
    	this.colunasResumoCard = (JRDesignRectangle) this.columnHeader.getElementByKey("colunasResumoCard");
    	
    	//divide o tamanho do card pela quantidade de colunas
    	this.widthColumn = this.colunasCard.getWidth()/qtdCol;
    	//pega a posição inicial do card
    	this.xInitialHeader = this.colunasCard.getX();
    	
    	//divide o tamanho do card de resumo de acordo com os tipos
    	if(this.tipo.equals(TODOS)) {
    		this.widthColumnResumo = this.colunasResumoCard.getWidth()/2;    		
    	}else {
    		this.widthColumnResumo = this.colunasResumoCard.getWidth();
    	}
    	//pega a posição inicial do card de resumo
    	this.xInitialHeaderResumo = this.colunasResumoCard.getX();
    	
    	columnCard(qtdCol);
    	columnCardResumo();
    	
    	return relatorioPrincipal;
	}
	
	//Monta e configura as colunas de datas
	private void columnCard(int qtdCol) throws JRException{
		
		int xUltimoAdd = this.xInitialHeader;
		JRDesignStaticText elemento = null;
		for(int count = 1; count <= qtdCol; count++) {
			if(this.tipo.equals(TODOS)) {
				this.columnHeader.addElement(montarColunaData(xUltimoAdd, yDefaultElementoColumn1, this.widthColumn, PROJETADO));
				this.columnHeader.addElement(montarColunaData(xUltimoAdd, yDefaultElementoColumn2, this.widthColumn, "10/07/2019"));
				this.columnHeader.addElement(montarColunaData((xUltimoAdd+this.widthColumn), yDefaultElementoColumn1, this.widthColumn, REALIZADO2));
				elemento = montarColunaData((xUltimoAdd+this.widthColumn), yDefaultElementoColumn2, this.widthColumn, "10/07/2019");
				this.columnHeader.addElement(elemento);
				xUltimoAdd = elemento.getX()+this.widthColumn;
			}else if(this.tipo.equals(PROJETADO)) {
				elemento = montarColunaData(xUltimoAdd, yDefaultElementoColumn1, this.widthColumn, PROJETADO);
				this.columnHeader.addElement(elemento);
				this.columnHeader.addElement(montarColunaData(xUltimoAdd, yDefaultElementoColumn2, this.widthColumn, "10/07/2019"));
				xUltimoAdd = elemento.getX()+this.widthColumn;
			}else {
				elemento = montarColunaData(xUltimoAdd, yDefaultElementoColumn1, this.widthColumn, REALIZADO2);
				this.columnHeader.addElement(elemento);
				this.columnHeader.addElement(montarColunaData(xUltimoAdd, yDefaultElementoColumn2, this.widthColumn, "10/07/2019"));
				xUltimoAdd = elemento.getX()+this.widthColumn;
			}
		}
		
		this.relatorioPrincipal.setColumnHeader(this.columnHeader);
	}
	
	private JRDesignStaticText montarColunaData(int x, int y, int width, String data) {
		JRElementConfig columnData = new JRElementConfig();
		columnData.setWidth(width);
		columnData.setHeight(15);
		columnData.setBold(true);
		columnData.setFontName(SANS_SERIF_FONT);
		columnData.setFontSize(8.0f);
		columnData.setBorderWidth(0.19f);
		columnData.setY(y);
		columnData.setX(x);
		columnData.setTextHorizontallAlign(HorizontalTextAlignEnum.CENTER);
		columnData.setVerticalTextAlignEnum(VerticalTextAlignEnum.MIDDLE);
		columnData.setText(data);
		return borderedStaticText(columnData);
	}
	//ternima a configuração das colunas de datas
	
	//Monta e configura as colunas de resumo
	private void columnCardResumo() throws JRException{
		
		if (this.tipo.equals(TODOS)) {
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn1, this.widthColumnResumo,  RESUMO));
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn2, this.widthColumnResumo, PROJETADO2));
			this.columnHeader.addElement(montarColunaData((this.xInitialHeaderResumo + this.widthColumnResumo), yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.columnHeader.addElement(montarColunaData((this.xInitialHeaderResumo + this.widthColumnResumo), yDefaultElementoColumn2, this.widthColumnResumo, REALIZADO));
		} else if (this.tipo.equals(PROJETADO)) {
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn2, this.widthColumnResumo, PROJETADO2));
		} else {
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.columnHeader.addElement(montarColunaData(this.xInitialHeaderResumo, yDefaultElementoColumn2, this.widthColumnResumo, REALIZADO));
		}

		this.relatorioPrincipal.setColumnHeader(this.columnHeader);
	}
	 
	
	
	//Configura style para as fields
	private JRDesignStaticText borderedStaticText(JRElementConfig config) {

		JRDesignStaticText field = new JRDesignStaticText();

		field.setHorizontalTextAlign(config.getTextHorizontallAlign());
		field.setVerticalTextAlign(config.getVerticalTextAlignEnum());
		field.setFontName(config.getFontName());
		field.setFontSize(config.getFontSize());
		field.setText(config.getText());
		field.getParagraph().setRightIndent(config.getRightIdent());
		field.getParagraph().setLeftIndent(config.getLeftIdent());
		field.getLineBox().getPen().setLineWidth(config.getBorderWidth());
		field.setBold(config.isBold());
		field.setX(config.getX());
		field.setY(config.getY());
		field.setWidth(config.getWidth());
		field.setHeight(config.getHeight());
		
		return field;

	}
	//termina a configuração dos fields
	
	private List<ColunmAgrupamentoRelDTO> montarDados() {
		ColunmAgrupamentoRelDTO dto = new ColunmAgrupamentoRelDTO();
		dto.setData(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		dto.setTipo(PROJETADO2);
		
		//Receita
		ReceitaLiquidaRelDTO receitaLiqDto = new ReceitaLiquidaRelDTO();
		receitaLiqDto.setNome("RECEITA LÍQUIDA INICIAL - NÃO VINCULADO");
		
		ReceitaDespesaMuniRelDTO receita = new ReceitaDespesaMuniRelDTO();
		receita.setNome("RECEITAS DO MUNICÍPIO");
		
		GrupoRelDTO grupo = new GrupoRelDTO();
		grupo.setNome("RECEITA PRÓPRIA");
		GrupoRelDTO grupo2 = new GrupoRelDTO();
		grupo2.setNome("RECEITA PRÓPRIA");
		
		SubGrupoRelDTO subGrupo = new SubGrupoRelDTO();
		subGrupo.setNome("IPTU");
		subGrupo.setValores(Arrays.asList(BigDecimal.TEN));
		SubGrupoRelDTO subGrupo2 = new SubGrupoRelDTO();
		subGrupo2.setNome("IPTU");
		subGrupo2.setValores(Arrays.asList(BigDecimal.ONE));
		
		grupo.setSubgrupos(Arrays.asList(subGrupo,subGrupo2));
		grupo.setTotalizadores(Arrays.asList(new BigDecimal(3)));
		grupo2.setSubgrupos(Arrays.asList(subGrupo,subGrupo2));
		grupo2.setTotalizadores(Arrays.asList(new BigDecimal(3)));
		
		receita.setGrupos(Arrays.asList(grupo, grupo2));
		
		//Despesa
		ReceitaDespesaMuniRelDTO despesa = new ReceitaDespesaMuniRelDTO();
		despesa.setNome("DESPESAS DO MUNICÍPIO");
		despesa.setGrupos(Arrays.asList(grupo, grupo2));
		
		receitaLiqDto.setReceitasMunicipio(Arrays.asList(receita));
		receitaLiqDto.setDespesasMunicipio(Arrays.asList(despesa));
		
		dto.setReceitasLiqInicial(Arrays.asList(receitaLiqDto));
		dto.setReceitasLiqFinal(Arrays.asList(receitaLiqDto));
		
		return Arrays.asList(dto);
	}
	

}
