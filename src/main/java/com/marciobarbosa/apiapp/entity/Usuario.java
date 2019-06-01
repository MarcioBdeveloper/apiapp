package com.marciobarbosa.apiapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT(10) NOT NULL")
	private Long id;
	private String nome;
	private String instagran;
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;

}
