package com.marciobarbosa.apiapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marciobarbosa.apiapp.entity.Usuario;
import com.marciobarbosa.apiapp.repository.UsuarioRepository;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public Optional<Usuario> findById(Long id) {
		return this.repository.findById(id);
	}
	
	public List<Usuario> findAll(){
		return this.repository.findAll();
	}
	
	public Usuario save(Usuario usuario) {
		return this.repository.save(usuario);
	}
	
	public void deleteById(Long id) {
		this.repository.deleteById(id);
	}
	
	public void delete(Usuario usuario) {
		this.repository.delete(usuario);
	}

}
