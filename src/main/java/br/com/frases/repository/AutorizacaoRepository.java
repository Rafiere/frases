package br.com.frases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.frases.entity.Autorizacao;

public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long>{

	public Autorizacao findByNome(String nome); //Essa busca retornará um objeto do tipo "Autorizacao" e receberá o parâmetro "nome", que é do tipo "String".
	
}