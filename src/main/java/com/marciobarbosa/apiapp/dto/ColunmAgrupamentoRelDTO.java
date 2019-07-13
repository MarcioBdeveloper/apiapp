package com.marciobarbosa.apiapp.dto;

import lombok.Data;

@Data
public class ColunmAgrupamentoRelDTO {
	
	private String data;
	private String tipo;
	ReceitaLiquidaRelDTO receitasLiqInicialVinculada;
	ReceitaLiquidaRelDTO receitasLiqFinalNvinculada;
	

}
