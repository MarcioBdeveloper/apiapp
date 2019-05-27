package com.marciobarbosa.apiapp.enums;

public enum TipoUsuario {
	
	
	CIDADAO("C"),
	ORGAO("O");

	TipoUsuario(String codigo) {
		this.codigo = codigo;
	}
	
	private final String codigo;

	public String getCodigo() {
		return codigo;
	}

}
