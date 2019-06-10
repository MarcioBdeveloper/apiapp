package com.marciobarbosa.apiapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.marciobarbosa.apiapp.entity.Denuncia;
import com.marciobarbosa.apiapp.entity.Usuario;

import lombok.Data;

@Data
public class UsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String instagran;
	private String tipo;
	private List<Denuncia> denuncias;
	
	public UsuarioDTO(Usuario usuario) {
		super();
		this.id = usuario.getId() != null ? usuario.getId() : null;
		this.nome = usuario.getNome() != null ? usuario.getNome() : null;
		this.instagran = usuario.getInstagran() != null ? usuario.getInstagran() : null;
		this.tipo = usuario.getTipo() != null ? usuario.getTipo().name() : null;
		this.denuncias = usuario.getDenuncias() != null ? usuario.getDenuncias() : new ArrayList<>();
	}
	
	public UsuarioDTO() {
		super();
	}

}
