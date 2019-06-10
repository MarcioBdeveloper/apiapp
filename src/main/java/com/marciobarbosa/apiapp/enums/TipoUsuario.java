package com.marciobarbosa.apiapp.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoUsuario {
	
	
	CIDADAO(1),
	ORGAO(2);

	TipoUsuario(int codigo) {
		this.codigo = codigo;
	}
	
	private final int codigo;
	
	public int getCodigo() {
		return this.codigo;
	}
	
	
	public static TipoUsuario getPorCodigo(String descricao) {
		if(descricao != null) {
			if(TipoUsuario.CIDADAO.name().equalsIgnoreCase(descricao)) {
				return TipoUsuario.CIDADAO;
			}
			if(TipoUsuario.ORGAO.name().equalsIgnoreCase(descricao)) {
				return TipoUsuario.ORGAO;
			}
		}
		return null;
	}

}
