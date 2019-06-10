package com.marciobarbosa.apiapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.marciobarbosa.apiapp.dto.DenunciaDTO;
import com.marciobarbosa.apiapp.dto.ImagenDTO;
import com.marciobarbosa.apiapp.dto.UsuarioDTO;

import lombok.Data;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Data
@Entity
@Table(name = "denuncia")
public class Denuncia implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT(10) NOT NULL")
	private Long id;
	private String titulo;
	private String descricao;
	@Column(columnDefinition = "VARCHAR(1000)")
	private String localizacao;
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Usuario> envolvidos;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Imagens> imagens;
	
	public Denuncia(DenunciaDTO dto) {
		super();
		this.id = dto.getId() != null ? dto.getId() : null;
		this.titulo = dto.getTitulo();
		this.descricao = dto.getDescricao();
		this.localizacao = dto.getLocalizacao();
		this.imagens = new ArrayList<>();
		this.envolvidos = new ArrayList<>();
		for (UsuarioDTO userDto : dto.getEnvolvidos()) {
			this.envolvidos.add(new Usuario(userDto));
		}
		if(dto.getImagens() != null) {
			for (ImagenDTO imgDto : dto.getImagens()) {
				this.imagens.add(new Imagens(imgDto));
			}			
		}
		
	}
	
	public Denuncia() {
		super();
	}

}
