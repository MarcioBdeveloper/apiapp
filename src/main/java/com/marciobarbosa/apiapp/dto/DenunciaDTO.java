package com.marciobarbosa.apiapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.marciobarbosa.apiapp.entity.Denuncia;
import com.marciobarbosa.apiapp.entity.Imagens;
import com.marciobarbosa.apiapp.entity.Usuario;

import lombok.Data;

@Data
public class DenunciaDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private String descricao;
	private String localizacao;
	private List<UsuarioDTO> envolvidos;
	private List<ImagenDTO> imagens;
	
	public DenunciaDTO(Denuncia denuncia) {
		super();
		this.id = denuncia.getId();
		this.titulo = denuncia.getTitulo();
		this.descricao = denuncia.getDescricao();
		this.localizacao = denuncia.getLocalizacao();
		this.envolvidos = new ArrayList<>();
		for(Usuario user : denuncia.getEnvolvidos()) {
			this.envolvidos.add(new UsuarioDTO(user));
		}
		this.imagens = new ArrayList<>();
		for(Imagens img : denuncia.getImagens()) {
			this.imagens.add(new ImagenDTO(img));
		}
	}
	
	public DenunciaDTO() {
		super();
	}

}
