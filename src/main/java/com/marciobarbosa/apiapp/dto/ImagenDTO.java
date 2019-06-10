package com.marciobarbosa.apiapp.dto;

import java.io.Serializable;

import com.marciobarbosa.apiapp.entity.Imagens;

import lombok.Data;

@Data
public class ImagenDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String path;
	
	public ImagenDTO(Imagens imagen) {
		super();
		this.id = imagen.getId();
		this.path = imagen.getPath();
	}
	
	public ImagenDTO() {
		super();
	}

}
