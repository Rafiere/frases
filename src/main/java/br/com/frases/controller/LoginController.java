package br.com.frases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.frases.security.JwtUtils;

@RestController //Essa anotação serve para informarmos ao Spring que essa classe será uma classe "Controller", assim, todos os métodos dessa classe estão habilitados a retornar o objeto no formato "JSON".
@CrossOrigin //Essa anotação permite que todos os métodos da classe "UsuarioController" sejam acessíveis por qualquer cliente HTTP. Essa anotação necessita de algumas configurações de segurança, que não foram ensinadas nesse curso.
public class LoginController {
	
	@Autowired //A anotação "@Autowired" explicita que o Spring será o responsável por utilizar o comando "new" no objeto "authManager" sempre que necessário.
	private AuthenticationManager authManager;

	@PostMapping(path = "/login") //A anotação "@PostMapping(path = "/login")" indica que esse método poderá ser acessado através da realização de uma requisição do tipo "POST" na URL "http://127.0.0.1:8080/frases/login".
	public UsuarioDTO login(@RequestBody UsuarioDTO login) throws JsonProcessingException { //A anotação "@RequestBody" indica que o valor do objeto "login", que está sendo passado como parâmetro para esse método, será enviado através do corpo da requisição do tipo "POST" que será realizada na URL "http://127.0.0.1:8080/frases/login".
		String username = login.getNome(); //O objeto "username" está recebendo o valor do atributo "nome" do objeto "login", que foi passado como parâmetro para esse método.
		if(username == null) { //Se o valor do atributo "nome" for "null", o objeto "username" receberá o valor do atributo "email" do objeto "login", que foi passado como parâmetro para esse método.
			username = login.getEmail();
		}
		Authentication credentials = new UsernamePasswordAuthenticationToken(username, login.getSenha()); //Estamos criando o objeto "credentials", que é do tipo "Authentication", esse objeto receberá o retorno do método construtor da classe "UsernamePasswordAuthenticationToken", assim, basicamente, o objeto "credentials" receberá o nome de usuário e a senha do usuário que está realizando o login.
		User usuario = (User) authManager.authenticate(credentials).getPrincipal(); //Estamos criando o objeto "usuario", que é do tipo "User", esse objeto receberá o retorno do método "authManager.authenticate(credentials).getPrincipal()", esse método verificará se as credenciais que estão dentro do objeto "credentials" são iguais as credenciais desse usuário que estão no banco de dados, se as credenciais forem iguais, esse método retornará um objeto do tipo "Object", que corresponderá ao usuário cujas credenciais de login foram inseridas corretamente, se as credenciais não forem iguais, uma exception será lançada. Após isso, se as credenciais estiverem corretas, o método "getPrincipal()" retornará apenas o objeto do tipo "Object" que contém o usuário autenticado. Depois disso, estamos utilizando o cast "(User)" para convertermos o retorno desse método, que é um objeto do tipo "Object", para um objeto do tipo "User", e estamos armazenando esse objeto dentro do objeto "usuario", que é do tipo "User".
		login.setSenha(null); //Após realizarmos a autenticação, como o atributo "senha" do objeto "login" não terá mais nenhuma utilidade, estamos definindo o valor desse atributo como "null".
		login.setToken(JwtUtils.generateToken(usuario)); //Estamos utilizando o método "JwtUtils.generateToken(usuario)" para gerarmos um token com base no objeto "usuario", que é do tipo "User", e que foi retornado após as credenciais inseridas no login estarem corretas, após isso, estamos inserindo esse token que foi gerado no atributo "token" do objeto "login".
		
		return login; //Estamos retornando o objeto "login" com o atributo "token" preenchido, ou seja, esse objeto do tipo "login" está com um token de acesso validado.
	}
	
}
