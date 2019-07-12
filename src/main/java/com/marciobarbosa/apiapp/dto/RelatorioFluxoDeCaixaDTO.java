package com.marciobarbosa.apiapp.dto;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class RelatorioFluxoDeCaixaDTO {

	private List<ColunmAgrupamentoRelDTO> dados;
	
	public RelatorioFluxoDeCaixaDTO(ColunmAgrupamentoRelDTO dto) {
		this.dados = Arrays.asList(dto);
	}
	
	public RelatorioFluxoDeCaixaDTO() {

	}
	
}
