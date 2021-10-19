package br.com.frases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrasesApplication {

	public static void main(String[] args) { //Esse método serve para iniciarmos a aplicação. Se não estivéssemos com o "Boot Dashboard", basta executarmos essa classe para colocarmos a aplicação em funcionamento.
		SpringApplication.run(FrasesApplication.class, args);
	}

}
