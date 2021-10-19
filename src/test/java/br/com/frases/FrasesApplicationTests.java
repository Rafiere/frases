package br.com.frases;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import br.com.frases.entity.Frase;
import br.com.frases.entity.Usuario;
import br.com.frases.repository.FraseRepository;
import br.com.frases.repository.UsuarioRepository;
import br.com.frases.service.FraseService;
import br.com.frases.service.UsuarioService;

@SpringBootTest //Essa anotação explicita que essa classe conterá vários testes.
@Transactional //Essa anotação explicita que o banco de dados poderá ser acessado e poderá sofrer alterações. Não entendi muito bem o propósito dessa anotação.
@Rollback //Essa anotação explicita que todos os dados que forem inseridos no banco de dados, através dos testes dessa classe, serão excluídos após o término dos testes. Essa anotação é muito útil para não poluirmos o banco de dados.
class FrasesApplicationTests {

	@Autowired //Essa anotação explicita que a dependência será injetada diretamente no objeto "usuarioRepo", dessa forma, não deveremos utilizar o "new UsuarioRepository()", pois essa anotação injetará automaticamente essa dependência.
	private UsuarioRepository usuarioRepo; //Essa dependência permitirá o acesso aos métodos que manipularão a tabela "usr_usuario".
	
	@Autowired //Essa anotação explicita que a dependência será injetada diretamente no objeto "fraseRepo", dessa forma, não deveremos utilizar o "new FraseRepository()", pois essa anotação injetará automaticamente essa dependência.
	private FraseRepository fraseRepo; //Essa dependência permitirá o acesso aos métodos que manipularão a tabela "frs_frase".
	
	@Autowired //Essa anotação explicita que a dependência será injetada diretamente no objeto "usuarioService", dessa forma, não deveremos utilizar o "new UsuarioService()", pois essa anotação injetará automaticamente essa dependência.
	private UsuarioService usuarioService; //Essa dependência permitirá o acesso aos métodos que estão sendo declarados na interface "UsuarioService". É uma boa prática de programação instanciarmos a interface, que, nesse caso, é a interface "UsuarioService", ao invés de instanciarmos a classe que implementa essa interface, que, nesse caso, é a classe "UsuarioServiceImpl".
	
	@Autowired //Essa anotação explicita que a dependência será injetada diretamente no objeto "fraseService", dessa forma, não deveremos utilizar o "new FraseService()", pois essa anotação injetará automaticamente essa dependência.
	private FraseService fraseService; //Essa dependência permitirá o acesso aos métodos que estão sendo declarados na interface "FraseService". É uma boa prática de programação instanciarmos a interface, que, nesse caso, é a interface "FraseService", ao invés de instanciarmos a classe que implementa essa interface, que, nesse caso, é a classe "FraseServiceImpl".
	
	@Test //Essa anotação explicita que o método abaixo é um teste do JUnit.
	void contextLoads() { //Esse é um teste padrão, ele serve para verificar apenas se a aplicação está subindo corretamente, ou seja, se o arquivo de configuração está funcionando corretamente.
	}
	
	@Test //Essa anotação explicita que o método abaixo é um teste do JUnit.
	void usuarioRepositorySaveTest() { //Esse teste simulará a criação de um novo objeto do tipo "Usuario" e o salvamento desse objeto no banco de dados.
		Usuario usuario = new Usuario(); //Estamos criando o usuário que será salvo.
		usuario.setNome("Testando Oliveira"); //Estamos preenchendo os atributos do objeto "usuario".
		usuario.setEmail("testandooliveira@teste.com");
		usuario.setSenha("teste123");
		
		usuarioRepo.save(usuario); //Estamos salvando o objeto "usuario" na tabela "usr_usuario".
		
		assertNotNull(usuario.getId()); //Para o teste ser concluído com sucesso, o valor do atributo "id" do objeto "usuario" não pode ser "null".
	}
	
	@Test //Essa anotação explicita que o método abaixo é um teste do JUnit.
	void fraseRepositorySaveTest() { //Esse teste criará e salvará um objeto do tipo "Usuario" na tabela "usr_usuario", após isso, criará e salvará um objeto do tipo "Frase" na tabela "frs_frase", preenchendo o atributo "autor" do objeto do tipo "Frase" com o objeto do tipo "Usuario" que será criado e salvo no banco de dados.
		Usuario usuario = new Usuario(); //Estamos criando o objeto "usuario", que é do tipo "Usuario" que será salvo na tabela "usr_usuario".
		usuario.setNome("Testando Gomes"); //Estamos preenchendo os atributos do objeto "usuario", que será salvo na tabela "usr_usuario".
		usuario.setEmail("testandogomes@teste.com");
		usuario.setSenha("teste321");
		
		usuarioRepo.save(usuario); //Estamos salvando o objeto "usuario" na tabela "usr_usuario", e, ao executarmos esse método, além de salvarmos o objeto "usuario" no banco de dados, estamos, de forma automática, atualizando esse objeto e preenchendo o valor do atributo "id" desse objeto.
		
		Frase frase = new Frase(); //Estamos criando o objeto "frase", que é do tipo "Frase", esse objeto será salvo na tabela "frs_frase".
		frase.setTitulo("A Frase do Teste"); //Estamos preenchendo os atributos do objeto "frase", que será salvo na tabela "frs_frase".
		frase.setConteudo("O teste é um teste, não é mesmo?");
		frase.setDataHora(new Date());
		frase.setAutor(usuario);
		
		fraseRepo.save(frase); //Estamos salvando o objeto "frase" na tabela "frs_frase", e, ao executarmos esse método, além de salvarmos o objeto "frase" no banco de dados, estamos, de forma automática, atualizando esse objeto e preenchendo o valor do atributo "id" desse objeto.
		
		assertNotNull(frase.getId()); //Para o teste ser concluído com sucesso, o valor do atributo "id" do objeto "frase" não pode ser "null".
	}
	
	@Test //Essa anotação serve para explicitar que o método abaixo é um teste do JUnit.
	void usuarioServiceNovoUsuarioTest() { //Esse teste criará um novo objeto do tipo "Usuario" através do método "usuarioService.novoUsuario()".
		Usuario usuario = usuarioService.novoUsuario("testando123", "testando@email.com", "teste1234", "ROLE_ADMIN"); //Estamos utilizando o método "usuarioService.novoUsuario()" para criarmos um novo objeto, cujos valores dos atributos serão os valores passados por parâmetro para esse método.
	
		assertNotNull(usuario.getId()); //Esse teste não será concluído com sucesso se o atributo "id" do objeto "usuario" tiver o valor "null". Caso isso ocorra, significa que algum erro aconteceu durante a execução do método "usuarioService.novoUsuario()".
	}
	
	@Test //Essa anotação serve para explicitar que o método abaixo é um teste do JUnit.
	void fraseServiceAdicionarFraseTest() { //Esse teste criará um novo objeto do tipo "Usuario", após isso, criará e preencherá os atributos de um novo objeto do tipo "Frase", inclusive, utilizaremos o atributo "nome" do objeto "usuario" que foi criado como valor do atributo "autor" do objeto "frase", assim, estaremos testando o método "fraseService.adicionarFrase()".
		Usuario usuario = usuarioService.novoUsuario("testando123", "testando@email.com", "teste1234", "ROLE_ADMIN"); //Estamos criando um objeto do tipo "Usuario" e preenchendo os seus atributos.
		Frase frase = fraseService.adicionarFrase("Uma frase inovadora!", "A inovação é inovadora por natureza, pois, se não fosse, a inovação não seria uma inovação.", usuario.getNome()); //Estamos criando um objeto do tipo "Frase" e preenchendo os seus atributos.
		
		assertNotNull(frase.getId()); //Esse teste não será concluído com sucesso se o atributo "id" do objeto "frase" tiver o valor "null". Caso isso ocorra, significa que algum erro aconteceu durante a execução do método "fraseServiceAdicionarFraseTest()".
	}
}
