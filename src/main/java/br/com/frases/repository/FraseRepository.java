package br.com.frases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import br.com.frases.entity.Frase;

public interface FraseRepository extends JpaRepository<Frase, Long>{

	@PreAuthorize("isAutenticated()") //Essa anotação permitirá que qualquer usuário que estiver logado, independente da sua autorização, possa acessar o método abaixo.
	public List<Frase> findByAutorNomeOrAutorEmail(String nome, String email); //Essa busca retornará um objeto do tipo "List<Frase>" que conterá todas as frases de um determinado autor. Esse autor será buscado através do valor do seu atributo "nome" ou do valor do seu atributo "email".
	
}
