package br.com.frases.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //As anotações "@Data", "@AllArgsConstructor" e "@NoArgsConstructor" estão relacionadas ao Lombok.
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO { //Receberemos um objeto no formato JSON e transformaremos um JSON em um objeto da classe "UsuarioDTO", por causa disso, todos os campos dessa classe são do tipo "String". 

	private String nome;
	
	private String email;
	
	private String senha;
	
	private String autorizacao;
	
	private String token;
	
}
