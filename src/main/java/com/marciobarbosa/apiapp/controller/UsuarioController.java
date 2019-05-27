package com.marciobarbosa.apiapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.marciobarbosa.apiapp.enums.TipoUsuario;
import com.marciobarbosa.apiapp.model.Usuario;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 26/05/2019
 * */

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController {
	
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Usuario> listaUsuarios(){
		List<Usuario> usuarios = new ArrayList<>();
		Usuario user = new Usuario();
		user.setNome("Marcio");
		user.setInstagran("@insta");
		user.setTipo(TipoUsuario.CIDADAO);
		usuarios.add(user);
		
		Usuario user2 = new Usuario();
		user.setNome("Rousy");
		user.setInstagran("@instaRousy");
		user.setTipo(TipoUsuario.CIDADAO);
		usuarios.add(user2);
		
		Usuario user3 = new Usuario();
		user.setNome("Cagepa");
		user.setInstagran("@instaCagepa");
		user.setTipo(TipoUsuario.ORGAO);
		usuarios.add(user3);
		
		return usuarios;
	}

}
