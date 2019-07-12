package com.marciobarbosa.apiapp.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SubGrupoRelDTO {
	
	private String nome;
	private List<BigDecimal> valores;

}
