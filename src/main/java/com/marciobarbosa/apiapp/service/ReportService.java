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
	private static final String REALIZADO = "REALIZADO";
	private static final String RESUMO = "RESUMO";
	private static final String PROJETADO = "PROJETADO";
	private static final String TODOS = "TODOS";
    private static final String SANS_SERIF_FONT = "SansSerif";
    
	private JasperDesign relatorioPrincipal;
	private JRDesignBand pageHeader;
	private static final Color colorBackGround = new Color(199,199,199);
	private static final Color colorSaldoFinal = new Color(222,222,222);
	private static final Color colorWhite = new Color(255,255,255);
	
	//cards
	private int widthColumn;
	private int widthColumnResumo;
	private static final int widthCardDatas = 500;
	private static final int widthCardResumo = 121;
	private static final int xInitCardDatas = 181;
	private static final int yInitCardDatas = 0;
	private static final int xInitCardResumo = 681;
	private static final int yIntitCardResumo = 0;
	private static final int heightCards = 30;

	
	//Detail
	private static final int widthDetail = 802;
	private static final int heightRetangleDetail = 15;
	private static final int xInitDetail = 0;
	private static final int yInitDetail = 30;
	
	private static final int yDefaultElementoColumn1 = 0;
	private static final int yDefaultElementoColumn2 = 15;
	
	private static final int leftIdentGrupo = 25;
	private static final int leftIdentSubGrupo = 35;
	
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
    			.withParameter("mesInicio", LocalDate.now().toString())
    			.withParameter("mesFim", LocalDate.now().toString())
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
    	
    	
    	//divide o tamanho do card pela quantidade de colunas
    	this.widthColumn = widthCardDatas/qtdCol;
    	
    	//divide o tamanho do card de resumo de acordo com os tipos
    	if(this.tipo.equals(TODOS)) {
    		this.widthColumnResumo = widthCardResumo/2;    		
    	}else {
    		this.widthColumnResumo = widthCardResumo;
    	}
    	
    	columnCard();
    	columnCardResumo();
    	montarDetail(dados);
    	
    	return relatorioPrincipal;
	}
	
	//Monta e configura as colunas de datas
	private void columnCard() throws JRException{
		
		JRDesignRectangle retReceitaInitLiq = new JRDesignRectangle();
		retReceitaInitLiq.setY(yInitCardDatas);
		retReceitaInitLiq.setX(xInitCardDatas);
		retReceitaInitLiq.setWidth(widthCardDatas);
		retReceitaInitLiq.setHeight(heightCards);
		retReceitaInitLiq.setBackcolor(colorBackGround);
		this.pageHeader.addElement(retReceitaInitLiq);
		
		int xUltimoAdd = xInitCardDatas;
		JRDesignStaticText elemento = null;
		for(ColunmAgrupamentoRelDTO dto : this.dadosRel) {
			this.pageHeader.addElement(montarColunaData(xUltimoAdd, yDefaultElementoColumn1, this.widthColumn, dto.getData()));
			elemento = montarColunaData(xUltimoAdd, yDefaultElementoColumn2, this.widthColumn, dto.getTipo());
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
		retReceitaInitLiq.setY(yIntitCardResumo);
		retReceitaInitLiq.setX(xInitCardResumo);
		retReceitaInitLiq.setWidth(widthCardResumo);
		retReceitaInitLiq.setHeight(heightCards);
		retReceitaInitLiq.setBackcolor(colorBackGround);
		this.pageHeader.addElement(retReceitaInitLiq);
		
		
		if (this.tipo.equals(TODOS)) {
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn1, this.widthColumnResumo,  RESUMO));
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn2, this.widthColumnResumo, PROJETADO));
			this.pageHeader.addElement(montarColunaData((xInitCardResumo + this.widthColumnResumo), yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData((xInitCardResumo + this.widthColumnResumo), yDefaultElementoColumn2, this.widthColumnResumo, REALIZADO));
		} else if (this.tipo.equals(PROJETADO)) {
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn2, this.widthColumnResumo, PROJETADO));
		} else {
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn1, this.widthColumnResumo, RESUMO));
			this.pageHeader.addElement(montarColunaData(xInitCardResumo, yDefaultElementoColumn2, this.widthColumnResumo, REALIZADO));
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
	
		this.pageHeader.addElement(montarRetangulo(yInitDetail, xInitDetail, colorBackGround));
		int yUltimoAdd = yInitDetail;
		
		for(ColunmAgrupamentoRelDTO dados : dto) {
			
			//Receita Liquida inicial - não Vinculada
			ReceitaLiquidaRelDTO dadosReceitaLiq = dados.getReceitasLiqFinalNvinculada();
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, dadosReceitaLiq.getNome()));
			yUltimoAdd += 15;
			
			//Receitas Municipio	
			ReceitaDespesaMuniRelDTO recMunicipio = dadosReceitaLiq.getReceitasMunicipio();
					
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, xInitDetail, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, recMunicipio.getNome()));
			yUltimoAdd += 15;
					
			for(GrupoRelDTO grupo : recMunicipio.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, grupo.getNome(), leftIdentGrupo);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupo : grupo.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, subGrupo.getNome(),leftIdentSubGrupo);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			
			//Despesas municipio
			ReceitaDespesaMuniRelDTO despMunicipio = dadosReceitaLiq.getDespesasMunicipio();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, xInitDetail, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, despMunicipio.getNome()));
			yUltimoAdd += 15;
			
			
			for(GrupoRelDTO grupo : despMunicipio.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, grupo.getNome(), leftIdentGrupo);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupo : grupo.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, subGrupo.getNome(), leftIdentSubGrupo);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			
			
			//Receita Liquida inicial - Vinculada
			ReceitaLiquidaRelDTO dadosReeitaVinculada = dados.getReceitasLiqInicialVinculada();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, xInitDetail, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, dadosReeitaVinculada.getNome()));
			yUltimoAdd += 15;
			
			//Receitas municipio
			ReceitaDespesaMuniRelDTO recMunicipioVinc = dadosReeitaVinculada.getReceitasMunicipio();
			
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, xInitDetail, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, recMunicipioVinc.getNome()));
			yUltimoAdd += 15;
					
			for(GrupoRelDTO grupoVinc : recMunicipioVinc.getGrupos()) {
						
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, grupoVinc.getNome(), leftIdentGrupo);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupoVinc : grupoVinc.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, subGrupoVinc.getNome(), leftIdentSubGrupo);
					elementoSub.setBackcolor(colorWhite);
					elementoSub.setFontSize(6.0f);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
			
			//Despesas municipio
			ReceitaDespesaMuniRelDTO despMunicipioVinc = dadosReeitaVinculada.getDespesasMunicipio();
			this.pageHeader.addElement(montarRetangulo(yUltimoAdd, xInitDetail, colorBackGround));
			this.pageHeader.addElement(montarColunaDescricao(xInitDetail, yUltimoAdd, despMunicipioVinc.getNome()));
			yUltimoAdd += 15;
			
			for(GrupoRelDTO grupoVinc : despMunicipioVinc.getGrupos()) {
				
				JRDesignStaticText elementoGrupo = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, grupoVinc.getNome(), leftIdentGrupo);
				elementoGrupo.setBackcolor(colorWhite);
				this.pageHeader.addElement(elementoGrupo);
				yUltimoAdd += 15;
						
				for(SubGrupoRelDTO subGrupoVinc : grupoVinc.getSubgrupos()) {
							
					JRDesignStaticText elementoSub = montarColunaDescricaoGrupo(xInitDetail, yUltimoAdd, subGrupoVinc.getNome(), leftIdentSubGrupo);
					elementoSub.setBackcolor(colorWhite);
					this.pageHeader.addElement(elementoSub);
					yUltimoAdd += 15;
							
				}
			}
		}
		
		this.relatorioPrincipal.setPageHeader(this.pageHeader);
		
	} 
	
	private JRDesignRectangle montarRetangulo(int y, int x, Color color) {
		JRDesignRectangle retReceitaInitLiq = new JRDesignRectangle();
		retReceitaInitLiq.setY(y);
		retReceitaInitLiq.setX(x);
		retReceitaInitLiq.setWidth(widthDetail);
		retReceitaInitLiq.setHeight(heightRetangleDetail);
		retReceitaInitLiq.setBackcolor(color);
		return retReceitaInitLiq;
	}
	
	private JRDesignStaticText montarColunaDescricao(int x, int y, String descricao) {
		JRElementConfig columnData = new JRElementConfig();
		columnData.setWidth(xInitCardDatas);
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
		columnData.setWidth(xInitCardDatas);
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
		
		dto.setData(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
