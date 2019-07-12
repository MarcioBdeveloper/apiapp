package com.marciobarbosa.apiapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class ColunmAgrupamentoRelDTO {
	
	private String data;
	private String tipo;
	List<ReceitaLiquidaRelDTO> receitasLiqInicial;
	List<ReceitaLiquidaRelDTO> receitasLiqFinal;
	

}
