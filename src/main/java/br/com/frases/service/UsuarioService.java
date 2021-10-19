package br.com.frases.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.frases.entity.Usuario;

public interface UsuarioService extends UserDetailsService { //Estamos herdando a interface "UserDetailsService", essa interface obrigará a implementação do método "loadUserByUsername()".

	public Usuario novoUsuario(String nome, String email, 
			String senha, String autorizacao); //Essa é a declaração do método "novoUsuario()", que será implementado na classe "UsuarioServiceImpl".
	
}
