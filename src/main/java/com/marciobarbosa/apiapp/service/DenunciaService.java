package com.marciobarbosa.apiapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	public Optional<Denuncia> findById(Long id){
		return this.repository.findById(id);
	}
	
	public List<Denuncia> findAll(){
		return this.repository.findAll();
	}
	
	public Denuncia save(Denuncia denuncia) {
		return this.repository.save(denuncia);
	}

	public void delete(Denuncia denuncia) {
		this.repository.delete(denuncia);
	}
	
	public void delete(Long id) {
		this.repository.deleteById(id);
	}
}
