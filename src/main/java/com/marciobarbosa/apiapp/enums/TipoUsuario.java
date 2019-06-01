package com.marciobarbosa.apiapp.enums;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

public enum TipoUsuario {
	
	
	C("C", "Cidadão"),
	O("O", "Orgão resposável");

	TipoUsuario(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	private final String codigo;
	private final String descricao;
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}
