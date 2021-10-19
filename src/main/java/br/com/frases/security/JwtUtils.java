package br.com.frases.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.boot.json.JsonParseException;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.frases.controller.UsuarioDTO;
import io.jsonwebtoken.Jwts;

public class JwtUtils {

	private static final String KEY = "spring.jwt.sec"; //Essa é a chave criptográfica que será utilizada na criação do token, porém, essa chave, em um projeto profissional, não deveria estar exposta aqui, e nem deveria ser tão simples igual essa chave. Esse método utilizará a palavra chave "static" pois a classe "JwtUtils" não precisa ser instanciada para esse método ser poder ser utilizado.
	
	public static String generateToken(User usuario) throws JsonProcessingException { //Esse método servirá para gerar o token de acesso com base no usuário, cuja autenticação foi aprovada pelo método "login()" da classe "LoginController".
		ObjectMapper mapper = new ObjectMapper(); //Esse objeto é um "jax", ele serve para, basicamente, ler e escrever objetos no formato JSON.
		UsuarioDTO usuarioSemSenha = new UsuarioDTO(); //Esse objeto servirá para inserirmos os valores dos atributos "nome" e "autorizacao", após isso, ele servirá como base para o objeto no formato JSON ser gerado, pois os objetos das classes "DTO" possuem apenas campos do tipo "String", ou seja, são utilizados para permitir a geração de objetos no formato JSON.
		usuarioSemSenha.setNome(usuario.getUsername()); //Estamos preenchendo o atributo "nome" do objeto "usuarioSemSenha" com o valor do atributo "username" do objeto "usuario". Esse objeto "usuario" é o objeto cuja autenticação foi aprovada, assim, esse objeto foi passado por parâmetro para esse método através do método "login()", da classe "LoginController".
		if(!usuario.getAuthorities().isEmpty()) { //Se a lista de autorizações do usuário não estiver vazia, estamos preenchendo o valor do atributo "autorizacao" do objeto "usuarioSemSenha" com a primeira autorização do usuário que foi passado para o método "tokenGenerate()". Estamos obtendo apenas uma das autorizações pois esse é um método de testes, pois o objeto "usuario" poderia ter mais de uma autorização. Caso quiséssemos obter todas as autorizações, o atributo "autorizacao" do objeto "usuarioSemSenha", que é do tipo "Autorizacao", deveria ser do tipo "List<Autorizacao>".
			usuarioSemSenha.setAutorizacao(usuario.getAuthorities().iterator().next().getAuthority());
		}
		String usuarioJson = mapper.writeValueAsString(usuarioSemSenha); //O método "mapper.writeValueAsString()" está gerando uma String do objeto "usuarioSemSenha", ou seja, está convertendo o objeto "usuarioSemSenha" para o tipo "String", e armazenando esse objeto, convertido numa String com o formato JSON, no objeto "usuarioJson". Esse objeto terá apenas o nome do usuário e as autorizações desse usuário, nesse exemplo, apenas uma das autorizações.
		Date agora = new Date(); //O objeto "agora", que é do tipo "Date", receberá a data atual. Esse objeto servirá para calcularmos quando o token expirará, posteriormente.
		Long hora = 1000L * 60L * 60L; //Estamos armazenando no objeto "hora", o valor, em milissegundos (ms), do período de 1 hora, esse período será o tempo que o token estará válido, após esse período, o token não estará mais válido.
		return Jwts.builder().claim("userDetails", usuarioJson) //O método "Jwts.builder()" serve para construirmos as partes que formarão o token. Dentro do método "claim()", deveremos inserir os parâmetros do token. O primeiro e único parâmetro que inseriremos é o "userDetails", que conterá os detalhes do usuário, esses detalhes estão armazenados no objeto "usuarioJson", esse objeto contém o nome de usuário e as autorizações desse usuário. Abaixo, definiremos outros parâmetros desse token que será gerado.
				   .setIssuer("br.com.frases") //O método "setIssuer()" serve para inserirmos o nome de quem está enviando o token, que, nesse caso, seria o site "frases.com.br".
				   .setSubject(usuario.getUsername()) //O assunto do token será o nome do usuário.
				   .setExpiration(new Date(agora.getTime() + hora)) //Estamos definindo a validade do token, assim, esse token terá a validade de uma hora. A validade desse token é formado pela hora atual somada com o período de mais uma hora.
				   .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, KEY) //Estamos assinando o token, essa assinatura servirá para validar se os dados do token não foram alterados. Criaremos a assinatura no formato "HS512", e utilizaremos a chave que está armazenada, de forma incorreta, dentro do objeto "KEY" para gerarmos essa criptografia.
				   .compact(); //Estamos compactando todas as informações acima em uma String, no formato JSON, que será enviada como retorno para esse método. Essa String será o token que foi criado.
	}
	
	public static User parseToken(String token) throws JsonParseException, JsonMappingException, IOException { //O método "parseToken()" receberá um token e abrirá esse token.
		ObjectMapper mapper = new ObjectMapper(); //O objeto "mapper", que é do tipo "ObjectMapper", servirá para abrirmos o token, que está no formato "JSON".
		String credentialsJson = Jwts.parser() //O método "parser" serve para abrirmos o JSON recebido, para isso, ele necessita da chave de criptografia e do token que será aberto.
				.setSigningKey(KEY) //Estamos definindo a chave de criptografia que será utilizada para tentar abrir o token.
				.parseClaimsJws(token) //O método "parseClaimsJws(token)" está lendo o token "token" e utilizando a chave de criptografia "KEY", que foi definida através do método "setSigningKey()". Se o token não for aberto com sucesso, uma exception será gerada.
				.getBody() //Se o token foi aberto com sucesso, o método "getBody()" obtém o corpo desse token.
				.get("UserDetails", String.class); //Estamos utilizando o método "get()" para extrairmos, do token que foi aberto, a parte do "userDetails", que contém as credenciais do usuário, após isso, estamos convertendo essas credenciais em um objeto do tipo "String", e armazenando essas credenciais dentro do objeto "credentialsJson".
		UsuarioDTO usuario = mapper.readValue(credentialsJson, UsuarioDTO.class); //Estamos criando o objeto "usuario", que é do tipo "UsuarioDTO", e estamos utilizando o método "mapper.readValue()" para lermos o objeto "credentialsJson", que é um objeto do tipo "String" no formato "JSON", e convertermos esses dados para o tipo "UsuarioDTO", esses dados serão armazenados no objeto "usuario", que é do tipo "UsuarioDTO".
		return (User) User.builder().username(usuario.getNome()) //Estaremos retornando um objeto do tipo "User", que será montado através do valor dos atributos "nome" e "autorizacao" do objeto "usuario", que é do tipo "UsuarioDTO". Estamos preenchendo o campo "password" com o valor "secret" pois é obrigatório que insiramos uma senha, mesmo que ela não seja utilizada.
				.password("secret")
				.authorities(usuario.getAutorizacao())
				.build(); //Estamos criando o objeto do tipo "Object", com os atributos "username", "password" e "authorities" preenchidos com os valores que foram obtidos através do objeto "usuario", que é do tipo "UsuarioDTO", esse objeto sofrerá o "cast" para o tipo "User" e será o retorno do método "parseToken()".
	}
}
