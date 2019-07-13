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
	
	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
    private static final String TEMPLETE = "\\fluxo-caixa-detalhado-gerencial.jrxml";
	private static final String REALIZADO = "REALIZADO";
	private static final String RESUMO = "RESUMO";
	private static final String PROJETADO = "PROJETADO";
	private static final String TODOS = "TODOS";
    private static final String SANS_SERIF_FONT = "SansSerif";
    
	private JasperDesign relatorioPrincipal;
	private JRDesignBand pageHeader;
	private static final Color colorBackGround = new Color(199,199,199);
	private static final Color colorWhite = new Color(255,255,255);
	private static final Color colorYellow = new Color(255,241,43);
	private static final Color colorBlue = new Color(61,96,252);
	private static final Color colorRed = new Color(255,43,43);
	
	//cards
	private int widthColumn;
	private int widthColumnResumo;
	private static final int WIDTH_CARD_DATAS = 500;
	private static final int WIDTH_CARD_RESUMO = 121;
	private static final int X_INIT_CARD_DATAS = 181;
	private static final int Y_INIT_CARD_DATAS = 0;
	private static final int X_INIT_CARD_RESUMO = 681;
	private static final int Y_INIT_CARD_RESUMO = 0;
	private static final int HEIGHT_CARDS = 30;

	
	//Detail
	private static final int WIDTH_DETAIL = 802;
	private static final int HEIGHT_RETANGLE_DETAIL = 15;
	private static final int X_INIT_DETAIL = 0;
	private static final int Y_INIT_DETAIL = 30;
	private static final int HEIGHT_MAX_DETAIL = 375;
	
	private static final int Y_DEFAULT_ELEMENT_COLUMN1 = 0;
	private static final int Y_DEFAULT_ELEMENT_COLUMN2 = 15;
	
	private static final int LEFT_IDENT_GRUPO = 25;
	private static final int LEFT_IDENT_SUB_GRUPO = 35;
	
	private String tipo;
	private List<ColunmAgrupamentoRelDTO> dadosRel;
    
	@Autowired
    private ContextualizedJasperFileResolver fileResolver;
	
	@Autowired
    private JasperReportBuilder jasperBuilder;
	
	public void montarRelatorio() throws JRException {
		
		Map<String, JasperReport> subreports = new HashMap<>();
    	this.dadosRel = montarDados();
		//TODO ajustar para passar a quantidade de colunas de acordo com o filtro
    	JasperReportBuilderSnapshot resultado = jasperBuilder.forReport(
    			JasperCompileManager.compileReport(montarReport(this.dadosRel, TODOS)))
    			.withParameter("mesInicio", LocalDate.now().format(format))
    			.withParameter("mesFim", LocalDate.now().plusDays(2).format(format))
    			.withParameter("tipoRel", "Tipo Rel Teste")
    			.withParameter("recurso", "Recurso Rel Teste")
    			.withParameter("tipoNatureza", "Tipo Natureza Rel Teste")
    			.withParameter("tipoProjecao", "Tipo Projeção Rel Teste")
    			.withParameter("apresentacaoGrupo", "Apresentação Rel Teste")
    			.fill(montarDados()).snapshot();
    	
    	try {
    		resultado.downloadPdf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JasperDesign montarReport(List<ColunmAgrupamentoRelDTO> dados, String tipo) throws JRException {
		
		this.tipo = tipo;
		int qtdCol = dados.size();
		
		this.relatorioPrincipal = JRXmlLoader.load(fileResolver.resolveFile(TEMPLETE));
    	this.pageHeader = (JRDesignBand) this.relatorioPrincipal.getPageHeader();
    	JRDesignBand titulo = (JRDesignBand) this.relatorioPrincipal.getTitle();
    	JRDesignStaticText tituloRel = (JRDesignStaticText) titulo.getElementByKey("keyTitulo");
    	tituloRel.setBold(true);
    	tituloRel.setFontSize(10.0f);
    	JRDesignStaticText cabecalho = (JRDesignStaticText) titulo.getElementByKey("keyCabecalho");
    	cabecalho.setBold(true);
    	cabecalho.setFontSize(10.0f);
    	JRDesignStaticText cabecalho2 = (JRDesignStaticText) titulo.getElementByKey("keyCabecalho2");
    	cabecalho2.setBold(true);
    	cabecalho2.setFontSize(10.0f);
    	JRDesignStaticText cabecalho3 = (JRDesignStaticText) titulo.getElementByKey("keyCabecalho3");
    	cabecalho3.setBold(true);
    	cabecalho3.setFontSize(10.0f);
    	
    	
    	titulo.addElement(tituloRel);
    	titulo.addElement(cabecalho);
    	titulo.addElement(cabecalho2);
    	titulo.addElement(cabecalho3);
    	this.relatorioPrincipal.setTitle(titulo);
    	
    	//divide o tamanho do card pela quantidade de colunas
    	this.widthColumn = WIDTH_CARD_DATAS/qtdCol;
    	
    	//divide o tamanho do card de resumo de acordo com os tipos
    	if(this.tipo.equals(TODOS)) {
    		this.widthColumnResumo = WIDTH_CARD_RESUMO/2;    		
    	}else {
    		this.widthColumnResumo = WIDTH_CARD_RESUMO;
    	}
    	
    	columnCard();
    	columnCardResumo();
    	montarDetail(dados);
    	
    	return relatorioPrincipal;
	}
	
	//Monta e configura as colunas de datas
	private void columnCard() throws JRException{
		
		JRDesignRectangle retReceitaInitLiq = new JRDesignRectangle();
		retReceitaInitLiq.setY(Y_INIT_CARD_DATAS);
		retReceitaInitLiq.setX(X_INIT_CARD_DATAS);
		retReceitaInitLiq.setWidth(WIDTH_CARD_DATAS);
		retReceitaInitLiq.setHeight(HEIGHT_CARDS);
		retReceitaInitLiq.setBackcolor(colorBackGround);
		this.pageHeader.addElement(retReceitaInitLiq);
		
		int xUltimoAdd = X_INIT_CARD_DATAS;
		JRDesignStaticText elemento = null;
		for(ColunmAgrupamentoRelDTO dto : this.dadosRel) {
			this.pageHeader.addElement(montarColunaData(xUltimoAdd, Y_DEFAULT_ELEMENT_COLUMN1, this.widthColumn, dto.getData()));
			elemento = montarColunaData(xUltimoAdd, Y_DEFAULT_ELEMENT_COLUMN2, this.widthColumn, dto.getTipo());
			this.pageHeader.addElement(elemento);
			xUltimoAdd = elemento.getX()+this.widthColumn;
		}
		
		this.relatorioPrincipal.setPageHeader(this.pageHeader);
	}
	
	private JRDesignStaticText montarColunaData(int x, int y, int width, String data) {
		JRElementConfig columnData = new JRElementConfig();
		columnData.setWidth(width);
		columnData.setHeight(15);
		columnData.setBold(true);
		columnData.setFontName(SANS_SERIF_FONT);
		columnData.setFontSize(7.0f);
		columnData.setBorderWidth(0.19f);
		columnData.setY(y);
		columnData.setX(x);
		columnData.setTextHorizontallAlign(HorizontalTextAlignEnum.RIGHT);
		columnData.setVerticalTextAlignEnum(VerticalTextAlignEnum.MIDDLE);
		columnData.setText(data);
		columnData.setRightIdent(5);
		return borderedStaticText(columnData);
	}
	//ternima a configuração das colunas de datas
	
	//Monta e configura as colunas de resumo
	private void columnCardResumo() throws JRException{
		
		JRDesignRectangle retReceitaInitLiq = new JRDesignRectangle();
		retReceitaInitLiq.setY(Y_INIT_CARD_RESUMO);
		retReceitaInitLiq.setX(X_INIT_CARD_RESUMO);
		retReceitaInitLiq.setWidth(WIDTH_CARD_RESUMO);
		retReceitaInitLiq.setHeight(HEIGHT_CARDS);
		retReceitaInitLiq.setBackcolor(colorBackGround);
		this.pageHeader.addElement(retReceitaInitLiq);
		
		
		if (this.tipo.equals(TODOS)) {
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN1, this.widthColumnResumo,  RESUMO));
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN2, this.widthColumnResumo, PROJETADO));
			this.pageHeader.addElement(montarColunaData((X_INIT_CARD_RESUMO + this.widthColumnResumo), Y_DEFAULT_ELEMENT_COLUMN1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData((X_INIT_CARD_RESUMO + this.widthColumnResumo), Y_DEFAULT_ELEMENT_COLUMN2, this.widthColumnResumo, REALIZADO));
		} else if (this.tipo.equals(PROJETADO)) {
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN2, this.widthColumnResumo, PROJETADO));
		} else {
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData(X_INIT_CARD_RESUMO, Y_DEFAULT_ELEMENT_COLUMN2, this.widthColumnResumo, REALIZADO));
		}

		this.relatorioPrincipal.setPageHeader(this.pageHeader);
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
	
	
	//monta e configura detail Receita Liquida
	private void montarDetail(List<ColunmAgrupamentoRelDTO> dto) {
	
		this.pageHeader.addElement(montarRetangulo(Y_INIT_DETAIL, X_INIT_DETAIL, colorBackGround));
		int yUltimoAdd = Y_INIT_DETAIL;
		
		for(ColunmAgrupamentoRelDTO dados : dto) {
			
			//Receita Liquida inicial - não Vinculada
			ReceitaLiquidaRelDTO dadosReceitaLiq = dados.getReceitasLiqFinalNvinculada();
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, dadosReceitaLiq.getNome()));
			yUltimoAdd += 15;
			
			//Receitas Municipio	
			ReceitaDespesaMuniRelDTO recMunicipio = dadosReceitaLiq.getReceitasMunicipio();
					
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, recMunicipio.getNome()));
			yUltimoAdd += 15;
					
			for(GrupoRelDTO grupo : recMunicipio.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, grupo.getNome(), LEFT_IDENT_GRUPO);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupo : grupo.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, subGrupo.getNome(),LEFT_IDENT_SUB_GRUPO);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			
			//Despesas municipio
			ReceitaDespesaMuniRelDTO despMunicipio = dadosReceitaLiq.getDespesasMunicipio();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, despMunicipio.getNome()));
			yUltimoAdd += 15;
			
			
			for(GrupoRelDTO grupo : despMunicipio.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, grupo.getNome(), LEFT_IDENT_GRUPO);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupo : grupo.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, subGrupo.getNome(), LEFT_IDENT_SUB_GRUPO);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorYellow));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, "SALDO LÍQUIDO FINAL: NÃO VINCULADO"));
			yUltimoAdd += 15;
			
			
			
			//Receita Liquida inicial - Vinculada
			ReceitaLiquidaRelDTO dadosReceitaVinculada = dados.getReceitasLiqInicialVinculada();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, dadosReceitaVinculada.getNome()));
			yUltimoAdd += 15;
			
			//Receitas municipio
			ReceitaDespesaMuniRelDTO recMunicipioVinc = dadosReceitaVinculada.getReceitasMunicipio();
			
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, recMunicipioVinc.getNome()));
			yUltimoAdd += 15;
					
			for(GrupoRelDTO grupoVinc : recMunicipioVinc.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, grupoVinc.getNome(), LEFT_IDENT_GRUPO);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupoVinc : grupoVinc.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, subGrupoVinc.getNome(), LEFT_IDENT_SUB_GRUPO);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			
			//Despesas municipio
			ReceitaDespesaMuniRelDTO despMunicipioVinc = dadosReceitaVinculada.getDespesasMunicipio();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, despMunicipioVinc.getNome()));
			yUltimoAdd += 15;
			
			for(GrupoRelDTO grupoVinc : despMunicipioVinc.getGrupos()) {
				
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, grupoVinc.getNome(), LEFT_IDENT_GRUPO);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupoVinc : grupoVinc.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(X_INIT_DETAIL, yUltimoAdd, subGrupoVinc.getNome(), LEFT_IDENT_SUB_GRUPO);
					elementoSub.setBackcolor(colorWhite);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorYellow));
			this.pageHeader.addElement(montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, "SALDO LÍQUIDO FINAL: VINCULADO"));
			yUltimoAdd += 15;
			
		}
		this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
		JRDesignStaticText elemntTotalReceita = montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, "TOTAL DAS RECEITAS (NÃO VINCULADO/VINCULADO)");
		elemntTotalReceita.setFontSize(6.0f);
		this.pageHeader.addElement(elemntTotalReceita);
		yUltimoAdd += 15;
		this.pageHeader.addElement(montarRetangulo(yUltimoAdd, X_INIT_DETAIL, colorBackGround));
		JRDesignStaticText elemntTotalDespesa = montarColunaDescricao(X_INIT_DETAIL, yUltimoAdd, "TOTAL DAS DESPESAS (NÃO VINCULADO/VINCULADO)");
		elemntTotalDespesa.setFontSize(6.0f);
		this.pageHeader.addElement(elemntTotalDespesa);
		yUltimoAdd += 15;
		
		
		this.relatorioPrincipal.setPageHeader(this.pageHeader);
		
	} 
	
	private JRDesignRectangle montarRetangulo(int y, int x, Color color) {
		JRDesignRectangle retReceitaInitLiq = new JRDesignRectangle();
		retReceitaInitLiq.setY(y);
		retReceitaInitLiq.setX(x);
		retReceitaInitLiq.setWidth(WIDTH_DETAIL);
		retReceitaInitLiq.setHeight(HEIGHT_RETANGLE_DETAIL);
		retReceitaInitLiq.setBackcolor(color);
		return retReceitaInitLiq;
	}
	
	private JRDesignStaticText montarColunaDescricao(int x, int y, String descricao) {
		JRElementConfig columnData = new JRElementConfig();
		columnData.setWidth(X_INIT_CARD_DATAS);
		columnData.setHeight(15);
		columnData.setBold(true);
		columnData.setFontName(SANS_SERIF_FONT);
		columnData.setFontSize(7.0f);
		columnData.setBorderWidth(0.19f);
		columnData.setY(y);
		columnData.setX(x);
		columnData.setTextHorizontallAlign(HorizontalTextAlignEnum.LEFT);
		columnData.setVerticalTextAlignEnum(VerticalTextAlignEnum.MIDDLE);
		columnData.setText(descricao);
		columnData.setLeftIdent(5);
		return borderedStaticText(columnData);
	}
	
	private JRDesignStaticText montarColunaDescricaoGrupo(int x, int y, String descricao, int leftIdent) {
		JRElementConfig columnData = new JRElementConfig();
		columnData.setWidth(X_INIT_CARD_DATAS);
		columnData.setHeight(15);
		columnData.setBold(false);
		columnData.setFontName(SANS_SERIF_FONT);
		columnData.setFontSize(6.0f);
		columnData.setBorderWidth(0.19f);
		columnData.setY(y);
		columnData.setX(x);
		columnData.setTextHorizontallAlign(HorizontalTextAlignEnum.LEFT);
		columnData.setVerticalTextAlignEnum(VerticalTextAlignEnum.MIDDLE);
		columnData.setText(descricao);
		columnData.setLeftIdent(leftIdent);
		return borderedStaticText(columnData);
	}
	
	//termina a configuração da Receita Liquida
	
	private List<ColunmAgrupamentoRelDTO> montarDados() {
		ColunmAgrupamentoRelDTO dto = new ColunmAgrupamentoRelDTO();
		
		dto.setData(LocalDate.now().format(format));
		dto.setTipo(PROJETADO);
		
		
		//Receita
		ReceitaLiquidaRelDTO receitaLiqNvinvuladoDto = new ReceitaLiquidaRelDTO();
		receitaLiqNvinvuladoDto.setNome("RECEITA LÍQUIDA INICIAL - NÃO VINCULADO");
		
		ReceitaLiquidaRelDTO receitaLiqVinculadoDto = new ReceitaLiquidaRelDTO();
		receitaLiqVinculadoDto.setNome("RECEITA LÍQUIDA INICIAL VINCULADO");
		
		ReceitaDespesaMuniRelDTO receita = new ReceitaDespesaMuniRelDTO();
		receita.setNome("RECEITAS DO MUNICÍPIO");
		
		GrupoRelDTO grupo = new GrupoRelDTO();
		grupo.setNome("RECEITA PRÓPRIA");
//		GrupoRelDTO grupo2 = new GrupoRelDTO();
//		grupo2.setNome("TRANSFERENCIA DA UNIÃO");
		
		SubGrupoRelDTO subGrupo = new SubGrupoRelDTO();
		subGrupo.setNome("IPTU");
		subGrupo.setValores(Arrays.asList(BigDecimal.TEN));
//		SubGrupoRelDTO subGrupo2 = new SubGrupoRelDTO();
//		subGrupo2.setNome("CONTAS PUBLICAS (ÁGUA/FONTE/ENERGIA)");
//		subGrupo2.setValores(Arrays.asList(BigDecimal.ONE));
		
		grupo.setSubgrupos(Arrays.asList(subGrupo));
		grupo.setTotalizadores(Arrays.asList(new BigDecimal(3)));
//		grupo2.setSubgrupos(Arrays.asList(subGrupo,subGrupo2));
//		grupo2.setTotalizadores(Arrays.asList(new BigDecimal(3)));
		
		receita.setGrupos(Arrays.asList(grupo));
		
		//Despesa
		ReceitaDespesaMuniRelDTO despesa = new ReceitaDespesaMuniRelDTO();
		despesa.setNome("DESPESAS DO MUNICÍPIO");
		despesa.setGrupos(Arrays.asList(grupo));
		
		receitaLiqVinculadoDto.setDespesasMunicipio(despesa);
		receitaLiqVinculadoDto.setReceitasMunicipio(receita);

		receitaLiqNvinvuladoDto.setDespesasMunicipio(despesa);
		receitaLiqNvinvuladoDto.setReceitasMunicipio(receita);
		
		dto.setReceitasLiqFinalNvinculada(receitaLiqNvinvuladoDto);
		dto.setReceitasLiqInicialVinculada(receitaLiqVinculadoDto);
	
		
		return Arrays.asList(dto);
	}
	

}
