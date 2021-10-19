package br.com.frases.controller;

public class View { //Dentro dessa classe, estamos declarando as classes "FraseResumo" e "FraseCompleta", que servirão como labels para formatar os objetos no formato JSON.

	public static class FraseResumo {} //Estamos criando o label "FraseResumo".
	
	public static class FraseCompleta extends FraseResumo {} //Estamos criando o label "FraseCompleta", que herda a classe "FraseResumo", assim, qualquer método ou atributo que utilizar a label "FraseCompleta", também estará utilizando a label "FraseResumo".
	
}
