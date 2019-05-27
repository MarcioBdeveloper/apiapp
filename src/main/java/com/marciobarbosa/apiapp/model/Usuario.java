package com.marciobarbosa.apiapp.model;

import com.marciobarbosa.apiapp.enums.TipoUsuario;

import lombok.Data;


@Data
public class Usuario {

	private String nome;
	private String instagran;
	private TipoUsuario tipo;

}
