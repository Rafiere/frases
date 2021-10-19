package br.com.frases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.frases.entity.Usuario;
import br.com.frases.service.UsuarioService;

@RestController //Essa anotação serve para informarmos ao Spring que essa classe será uma classe "Controller", assim, todos os métodos dessa classe estão habilitados a retornar o objeto no formato "JSON".
@RequestMapping("/usuario") //Essa anotação serve para mapearmos essa classe, dessa forma, para acessarmos os métodos dessa classe, deveremos realizar requisições, dos tipos que cada método exige, na URL "http://127.0.0.1:8080/frases/usuario".
@CrossOrigin //Essa anotação permite que todos os métodos da classe "UsuarioController" sejam acessíveis por qualquer cliente HTTP. Essa anotação necessita de algumas configurações de segurança, que não foram ensinadas nesse curso.
public class UsuarioController {

	@Autowired //A anotação "@Autowired" explicita que o Spring será o responsável por utilizar o comando "new" no objeto "usuarioService" sempre que necessário.
	private UsuarioService usuarioService;
	
	@PostMapping("/novo") //Essa anotação explicita que esse método poderá ser acessado através da realização de uma requisição do tipo "POST" na URL "http://127.0.0.1:8080/frases/usuario/novo". No corpo dessa requisição, deverá ser enviado um objeto do tipo "UsuarioDTO", no formato "JSON".
	public Usuario cadastrarUsuario(@RequestBody UsuarioDTO usuario) { //Esse método receberá o objeto "usuario", que será do tipo "UsuarioDTO", através do corpo da requisição do tipo "POST", e, com base nos atributos desse objeto recebido, esse método criará um novo objeto do tipo "Usuario" com os atributos cujos valores serão iguais ao objeto do tipo "UsuarioDTO" que foi recebido através do corpo da requisição "POST" realizada. Após criar esse objeto do tipo "Usuario", esse método retornará esse objeto que foi criado, porém, no formato "JSON".
		return usuarioService.novoUsuario(usuario.getNome(), usuario.getEmail(),
				 usuario.getSenha(), usuario.getAutorizacao());
	}
}
