package com.marciobarbosa.apiapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReceitaLiquidaRelDTO {

	private String nome;
	private List<ReceitaDespesaMuniRelDTO> receitasMunicipio;
	private List<ReceitaDespesaMuniRelDTO> despesasMunicipio;
	
}
