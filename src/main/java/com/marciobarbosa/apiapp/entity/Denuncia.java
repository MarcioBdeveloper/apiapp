package com.marciobarbosa.apiapp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
public class Denuncia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT(10) NOT NULL")
	private Long id;
	private String titulo;
	private String descricao;
	@Column(columnDefinition = "VARCHAR(1000)")
	private String localizacao;
	@ManyToMany
	private List<Usuario> envolvidos;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Imagens> imagens;

}
