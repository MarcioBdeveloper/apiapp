package com.marciobarbosa.apiapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.marciobarbosa.apiapp.dto.ImagenDTO;

import lombok.Data;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Data
@Entity
@Table(name = "imagens")
public class Imagens implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT(10) NOT NULL")
	private Long id;
	private String path;
	
	public Imagens(ImagenDTO dto) {
		super();
		this.id = dto.getId() != null ? dto.getId() : null;
		this.path = dto.getPath();
	}
	
	public Imagens() {
		super();
	}
	
}
