package br.com.frases.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //As anotações "@Data", "@AllArgsConstructor" e "@NoArgsConstructor" estão relacionadas ao Lombok.
@AllArgsConstructor
@NoArgsConstructor

public class FraseDTO { //Receberemos um objeto no formato JSON e transformaremos um JSON em um objeto da classe "FraseDTO", por causa disso, todos os campos dessa classe são do tipo "String".

	private String usuario;
	
	private String titulo;
	
	private String conteudo;
	
}
