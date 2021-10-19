package br.com.frases.service;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.frases.entity.Autorizacao;
import br.com.frases.entity.Usuario;
import br.com.frases.repository.AutorizacaoRepository;
import br.com.frases.repository.UsuarioRepository;

@Service("usuarioService") //Essa anotação é apenas semântica, e serve para explicitar que essa classe é um serviço. Não entendi o motivo de passarmos o parâmetro "usuarioService" para essa anotação.
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired //Essa anotação serve para explicitarmos que o objeto "usuarioRepo" terá a funcionalidade de injeção de dependência sendo controlada, de forma automática, pelo Spring, assim, não precisaremos utilizar o comando "new UsuarioRepository()".
	private UsuarioRepository usuarioRepo;
	
	@Autowired //Essa anotação serve para explicitarmos que o objeto "autorizacaoRepo" terá a funcionalidade de injeção de dependência sendo controlada, de forma automática, pelo Spring, assim, não precisaremos utilizar o comando "new AutorizacaoRepository()".
	private AutorizacaoRepository autorizacaoRepo;
	
	@Autowired //Essa anotação serve para explicitarmos que o objeto "passEncoder" terá a funcionalidade de injeção de dependência sendo controlada, de forma automática, pelo Spring, assim, não precisaremos utilizar o comando "new AutorizacaoRepository()".
	private PasswordEncoder passEncoder; //Esse objeto permitirá que salvemos as senhas dos usuários, no banco de dados, utilizando um codificador de senhas, assim, no próprio banco de dados, a senha não será salva da forma que foi digitada pelo usuário, e sim, em uma forma codificada, com o objetivo de proteger essa senha.
	
	@Transactional //Essa anotação serve para executarmos todos os comandos que manipularão o banco de dados na ordem que eles estão sendo utilizados, assim, ao invés de criarmos várias transações, criaremos apenas uma transação com vários comandos SQL.
	@Override //Estamos sobrescrevendo o método "novoUsuario()", que foi declarado na interface "UsuarioService".
	@PreAuthorize("hasRole('ROLE_ADMIN')") //Essa anotação permite que o método "novoUsuario()" seja acessado apenas pelos usuários que tenham a autorização "ROLE_ADMIN".
	public Usuario novoUsuario(String nome, String email, String senha, String autorizacao) {
		
		Autorizacao aut = autorizacaoRepo.findByNome(nome); //Estamos, primeiramente, verificando se a autorização que foi passada como parâmetro para o método "novoUsuario()" existe na tabela "aut_autorizacao". Se não existir nenhuma autorização com o nome que foi passado como parâmetro para esse método, será retornado o valor "null".
		
		if(aut == null) { //Se não existir nenhum objeto do tipo "Autorizacao" cujo atributo "nome" for igual ao valor do parâmetro "autorizacao" que foi passado para o método "novoUsuario()", criaremos e salvaremos um novo objeto do tipo "Autorizacao" na tabela "aut_autorizacao", com o valor do atributo "nome" igual ao valor do parâmetro "autorizacao" que foi passado para o método "novoUsuario()".
			aut = new Autorizacao(); //Estamos instanciando a classe "Autorizacao" e armazenando esse objeto no objeto "aut".
			aut.setNome(autorizacao); //Estamos definindo o nome do objeto "aut" como o valor do parâmetro "autorizacao", que foi passado como parâmetro para o método "novoUsuario()".
			autorizacaoRepo.save(aut); //Estamos salvando a nova autorização na tabela "aut_autorizacao" e estamos inserindo o valor do atributo "id" do objeto que foi salvo.
		}
		
		Usuario usuario = new Usuario(); //Estamos criando um novo objeto do tipo "Usuario", que será preenchido com os valores que foram passados como parâmetro para o método "novoUsuario()", e que, posteriormente, será salvo na tabela "usr_usuario".
		usuario.setNome(nome); //Estamos definindo o valor do atributo "nome" do objeto do tipo "Usuario" que será salvo.
		usuario.setEmail(email); //Estamos definindo o valor do atributo "email" do objeto do tipo "Usuario" que será salvo.
		usuario.setSenha(passEncoder.encode(senha)); //Estamos definindo o valor do atributo "senha" do objeto do tipo "Usuario" que será salvo. Estamos utilizando o método "passEncoder.encode(senha)" para salvarmos a senha, no banco de dados, de forma codificada, ou seja, não salvaremos a senha de modo literal, e sim, de uma forma codificada.
		usuario.setAutorizacoes(new HashSet<Autorizacao>()); //Estamos preenchendo o valor do atributo "autorizacoes", que é uma lista do tipo "Set<Autorizacao>", com uma lista do tipo "HashSet<Autorizacao>", que, segundo a documentação, um "HashSet<Autorizacao>" equivale a um "Set<Autorizacao>" sem nenhum objeto dentro, com 16 espaços e com um "load factor" de "0.75", por padrão.
		usuario.getAutorizacoes().add(aut); //Estamos, dentro do atributo "autorizacoes", que é do tipo "HashSet<Autorizacao>", inserindo a autorização que foi criada ou que foi buscada e obtida da tabela "aut_autorizacoes".
		
		usuarioRepo.save(usuario); //Estamos salvando o objeto do tipo "Usuario" no banco de dados, além de preenchermos o valor do atributo "id" desse objeto, de forma automática.
		
		return usuario; //Estamos retornando o objeto "usuario" que foi criado e salvo na tabela "usr_usuario".	
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Esse método receberá o objeto "username", que é do tipo "String", e que poderá ser o nome de usuário ou o email de um determinado usuário, após isso, esse método servirá para fazer a autenticação, ou seja, verificar se um objeto do tipo "usuario", cujo valor dos atributos "nome" ou "email" seja igual ao valor do objeto "username", existe na tabela "usr_usuario". Se não existir, uma exception será retornada, se existir, esse método obterá o valor dos atributos "nome" ou "email", "senha" e "autorizacoes", armazenará esses valores em um objeto do tipo "UserDetails" e retornará esse objeto.
		
		Usuario usuario = usuarioRepo.findTopByNomeOrEmail(username, username); //Estamos verificando se existe um objeto do tipo "usuario" cujo valor dos atributos "nome" ou "email" seja igual ao valor que foi passado como parâmetro para o objeto "username".
		
		if(usuario == null) { //Se a pesquisa não retornar nenhum objeto do tipo "Usuario", a exception "UsernameNotFoundException" será lançada.
			throw new UsernameNotFoundException("O usuário " + username + " não foi encontrado!");
		}
		
		return User.builder() //Estamos começando a construir o objeto do tipo "UserDetails", que conterá o nome de usuário, a senha e a autorização do objeto do tipo "Usuario" que está no objeto "usuario".
				.username(username) //Estamos definindo o nome de usuário desse objeto do tipo "UserDetails".
				.password(usuario.getSenha()) //Estamos definindo a senha desse objeto do tipo "UserDetails".
				.authorities(usuario.getAutorizacoes() //Estamos obtendo a coleção "autorizacoes", que é do tipo "Set<Autorizacao>" e que contém todas as autorizações, com os seus respectivos IDs, desse objeto do tipo "Usuario".
							.stream()
							.map(Autorizacao::getNome) //Estamos obtendo apenas o valor do atributo "nome" de cada autorização existente para esse objeto do tipo "Usuario", excluindo o atributo "ID".
							.collect(Collectors.toList())
							.toArray(new String[usuario
												.getAutorizacoes()
												.size()])) //Estamos inserindo os valores do atributo "nome" de cada autorização existente dentro de um array do tipo "String".
							.build(); //Estamos montando esse objeto do tipo "UserDetails" que foi criado nesse método.
	}
}
