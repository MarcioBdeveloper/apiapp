package com.marciobarbosa.apiapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReceitaDespesaMuniRelDTO {
	
	private String nome;
	private List<GrupoRelDTO> grupos;

}
