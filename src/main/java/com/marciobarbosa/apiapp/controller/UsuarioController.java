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
		user2.setNome("Rousy");
		user2.setInstagran("@instaRousy");
		user2.setTipo(TipoUsuario.CIDADAO);
		usuarios.add(user2);
		
		Usuario user3 = new Usuario();
		user3.setNome("Cagepa");
		user3.setInstagran("@instaCagepa");
		user3.setTipo(TipoUsuario.ORGAO);
		usuarios.add(user3);
		
		return usuarios;
	}

}
