package com.marciobarbosa.apiapp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.marciobarbosa.apiapp.dto.UsuarioDTO;
import com.marciobarbosa.apiapp.enums.TipoUsuario;

import lombok.Data;


/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT(10) NOT NULL")
	private Long id;
	private String nome;
	private String instagran;
	@Enumerated(EnumType.ORDINAL)
	private TipoUsuario tipo;
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Denuncia> denuncias;
	
	public Usuario(UsuarioDTO dto) {
		super();
		this.id = dto.getId() != null ? dto.getId() : null;
		this.nome = dto.getNome();
		this.instagran = dto.getInstagran();
		this.tipo = TipoUsuario.getPorCodigo(dto.getTipo());
		this.denuncias = dto.getDenuncias();
	}

	public Usuario() {
		super();
	}
	
}
