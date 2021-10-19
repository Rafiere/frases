package br.com.frases.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.frases.entity.Frase;
import br.com.frases.repository.FraseRepository;
import br.com.frases.service.FraseService;

@RestController //Essa anotação serve para informarmos ao Spring que essa classe será uma classe "Controller", assim, todos os métodos dessa classe estão habilitados a retornar o objeto no formato "JSON".
@RequestMapping("/frase") //Essa anotação serve para mapearmos essa classe, dessa forma, para acessarmos os métodos dessa classe, deveremos realizar requisições, dos tipos que cada método exige, na URL "http://127.0.0.1:8080/frases/frase".
@CrossOrigin //Essa anotação permite que todos os métodos da classe "UsuarioController" sejam acessíveis por qualquer cliente HTTP. Essa anotação necessita de algumas configurações de segurança, que não foram ensinadas nesse curso.
public class FraseController {

	@Autowired //A anotação "@Autowired" explicita que o Spring será o responsável por utilizar o comando "new" no objeto "fraseService" sempre que necessário.
	private FraseService fraseService;
	
	@Autowired //A anotação "@Autowired" explicita que o Spring será o responsável por utilizar o comando "new" no objeto "fraseRepository" sempre que necessário.
	private FraseRepository fraseRepository;
	
	@PostMapping("/nova") //Essa anotação explicita que esse método poderá ser acessado através da realização de uma requisição do tipo "POST" na URL "http://127.0.0.1:8080/frases/frase/nova". No corpo dessa requisição, deveremos inserir, no formato "JSON", o objeto do tipo "FraseDTO" que será salvo no banco de dados. Esse método retornará, no formato "JSON", a frase que foi salva no banco de dados.
	@JsonView(View.FraseCompleta.class) //Essa anotação, dentro de um método que está dentro de uma classe que possui a anotação "@RestController", ou seja, cujo retorno dos métodos será um objeto no formato "JSON", explicita que o JSON retornado por esse objeto deverá possuir apenas os campos que a label "FraseCompleta" definir.
	public Frase cadastrarFrase(@RequestBody FraseDTO frase) { //Esse método criará um novo objeto do tipo "Frase" com os atributos do objeto "frase", do tipo "FraseDTO", que foi passado através do corpo da requisição "POST" que foi realizada na URL "http://127.0.0.1:8080/frases/frase/nova", e salvará esse objeto, que foi criado, no banco de dados, após isso, esse método retornará, no formato "JSON", o objeto que foi criado e salvo no banco de dados.
		return fraseService.adicionarFrase(frase.getTitulo(),
				 frase.getConteudo(), frase.getUsuario());
	}
	
	@GetMapping("/busca/{autor}") //Essa anotação explicita que esse método poderá ser acessado através da realização de uma requisição do tipo "GET" na URL "http://127.0.0.1:8080/frases/frase/busca/{autor}", e, ao invés de escrevermos "{autor}", deveremos inserir o valor do atributo "nome" ou do atributo "email" de algum objeto do tipo "Usuario", dessa forma, será feita uma busca no banco de dados e esse serviço retornará todos os objetos do tipo "Frase" que estiverem relacionados ao nome ou o email do usuário que foi passado através da URL.
	@JsonView(View.FraseCompleta.class) //O objeto do tipo "List<Frase>" que será retornado por esse método, utilizará a label "FraseCompleta", assim, apenas os atributos que utilizarem essa label serão exibidos nesse objeto, no formato JSON, que será retornado por esse método.
	public List<Frase> buscarPorTitulo(@PathVariable("autor") String autor){ //A anotação "@PathVariable("autor")" explicita que o valor do parâmetro "autor", que será passado via URL, será armazenado no objeto "autor", que será do tipo "String".
		
		return fraseRepository.findByAutorNomeOrAutorEmail(autor, autor); //Com base no objeto do tipo "String" que foi passado para o método "buscarPorTitulo()", será realizada uma busca utilizando o valor do objeto "autor" para verificar se esse valor é igual aos atributos "nome" ou "email" de algum objeto do tipo "Usuario", se for, será retornada uma lista, do tipo "List<Frase>", contendo todos os objetos do tipo "Frase" que estão relacionados a esse autor, e esse objeto do tipo "List<Frase>" será retornado, no formato JSON, pelo método "buscarPorTitulo()". Não deveríamos utilizar um método de uma classe "Repository" dentro de um método de uma classe "Controller" apenas se utilizarmos esse método com alguma regra de negócio, porém, como não estamos inserindo nenhuma regra de negócio dentro desse método, apenas retornando o retorno do método "fraseRepository.findByAutorNomeOrAutorEmail()", poderemos utilizar um método de uma classe "Repository" dentro de um método de uma classe "Controller" normalmente.
	}
	
}
