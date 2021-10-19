package br.com.frases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.frases.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Usuario findTopByNomeOrEmail(String nome, String email); //Essa busca retornará um objeto do tipo "Usuario", e, para efetuar a pesquisa, receberá um objeto do tipo "nome" e um objeto do tipo "email" como parâmetros.
	
}
