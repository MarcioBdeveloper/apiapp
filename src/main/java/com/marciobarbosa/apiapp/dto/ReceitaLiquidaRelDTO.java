package com.marciobarbosa.apiapp.dto;

import lombok.Data;

@Data
public class ReceitaLiquidaRelDTO {

	private String nome;
	private ReceitaDespesaMuniRelDTO receitasMunicipio;
	private ReceitaDespesaMuniRelDTO despesasMunicipio;
	
}
