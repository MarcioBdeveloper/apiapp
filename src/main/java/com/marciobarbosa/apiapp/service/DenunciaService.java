package com.marciobarbosa.apiapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marciobarbosa.apiapp.dto.DenunciaDTO;
import com.marciobarbosa.apiapp.entity.Denuncia;
import com.marciobarbosa.apiapp.repository.DenunciaRepository;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Service
public class DenunciaService {
	
	@Autowired
	private DenunciaRepository repository;
	
	
	public DenunciaDTO findById(Long id){
		Optional<Denuncia> denuncia = this.repository.findById(id);
		if(denuncia.get() != null) {
			return new DenunciaDTO(denuncia.get());
		}
		return new DenunciaDTO();
	}
	
	public List<DenunciaDTO> findAll(){
		 List<DenunciaDTO> dtos = new ArrayList<>();
		for (Denuncia denuncia : this.repository.findAll()) {
			dtos.add(new DenunciaDTO(denuncia));
		}
		return dtos;
	}
	
	public DenunciaDTO save(DenunciaDTO denunciaDTO) {
		Denuncia denuncia = new Denuncia(denunciaDTO);
		denuncia = this.repository.saveAndFlush(denuncia);
		DenunciaDTO dto = new DenunciaDTO(denuncia);
		return dto;
	}
	
	public void delete(Denuncia denuncia) {
		this.repository.delete(denuncia);
	}
	
	public void delete(Long id) {
		this.repository.deleteById(id);
	}
}
