package com.marciobarbosa.apiapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marciobarbosa.apiapp.dto.DenunciaDTO;
import com.marciobarbosa.apiapp.entity.Denuncia;
import com.marciobarbosa.apiapp.service.DenunciaService;


/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 * */

@RestController
@CrossOrigin
@RequestMapping("/denuncia")
public class DenunciaController {

	@Autowired
	private DenunciaService service;
	
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DenunciaDTO> listaDenuncias(){
		List<DenunciaDTO> denuncias = new ArrayList<>();
		denuncias = this.service.findAll();
		return denuncias;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public DenunciaDTO salvar(@RequestBody DenunciaDTO denuncia) {
		return this.service.save(denuncia);
	}
	
	@RequestMapping(value = "/alterar", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public DenunciaDTO alterar(@RequestBody DenunciaDTO denuncia) {
		return this.service.save(denuncia);
	}
	
	@RequestMapping(value = "/deletar", method = RequestMethod.DELETE)
	public void deletar(@RequestParam Long id) {
		this.service.delete(id);
	}
	
	
}
