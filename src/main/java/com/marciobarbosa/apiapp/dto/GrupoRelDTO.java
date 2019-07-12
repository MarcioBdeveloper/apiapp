package com.marciobarbosa.apiapp.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class GrupoRelDTO {
	
	private String nome;
	private List<SubGrupoRelDTO> subgrupos;
	private List<BigDecimal> totalizadores;

}
