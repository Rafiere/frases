package br.com.frases.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.frases.entity.Frase;
import br.com.frases.entity.Usuario;
import br.com.frases.repository.FraseRepository;
import br.com.frases.repository.UsuarioRepository;

@Service("fraseService") //Essa anotação é apenas semântica, e serve para explicitar que essa classe é um serviço. Não entendi o motivo de passarmos o parâmetro "fraseService" para essa anotação.
public class FraseServiceImpl implements FraseService {

	@Autowired //Essa anotação explicita que o objeto "usuarioRepo" será injetado pelo Spring de forma automática.
	UsuarioRepository usuarioRepo;
	
	@Autowired //Essa anotação explicita que o objeto "fraseRepo" será injetado pelo Spring de forma automática.
	FraseRepository fraseRepo;
	
	@Transactional //Essa anotação serve para executarmos todos os comandos que manipularão o banco de dados na ordem que eles estão sendo utilizados, assim, ao invés de criarmos várias transações, criaremos apenas uma transação com vários comandos SQL.
	@Override //Estamos sobrescrevendo o método "adicionarFrase()", que foi declarado na interface "FraseService".
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") //A anotação "@PreAuthorize" exige que, para o método "adicionarFrase()" seja executado, o usuário que tentar executar esse método deve ter uma permissão especial. O parâmetro "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')" obriga que, para que o método "adicionarFrase()" seja executado, o usuário que está tentando executar esse método deve ter a autorização "ROLE_ADMIN" ou a autorização "ROLE_USER".
	public Frase adicionarFrase(String titulo, String conteudo,
			 String autor) {
		
		Usuario usuario = usuarioRepo.findTopByNomeOrEmail(autor, autor); //Primeiramente, estamos verificando se o objeto "autor" existe na tabela "usr_usuario", ou seja, se o autor, que, supostamente, está criando a frase, é um usuário cadastrado.
		
		if(usuario == null) { //Se a busca pelo objeto "autor" não retornar nenhum usuário, lançaremos a exception "UsernameNotFoundException", dessa forma, a execução do código será interrompida. Sempre que um erro for gerado, deveremos gerar uma exception, para evitar que a detecção do problema seja postergada, e o problema possa ser irrastreável depois de um tempo.
			throw new UsernameNotFoundException("Usuário com identificador " + autor + " não foi encontrado.");
		}
		
		Frase frase = new Frase(); //Se o autor da frase existir no banco de dados, criaremos o objeto "frase", que é do tipo "Frase", e preencheremos os atributos desse objeto com todos os valores que foram passados através do método "adicionarFrase()".
		frase.setTitulo(titulo);
		frase.setConteudo(conteudo);
		frase.setDataHora(new Date());
		frase.setAutor(usuario);
		
		fraseRepo.save(frase); //Após preenchermos todos os atributos do objeto "frase", estamos salvando esse objeto na tabela "frs_frase".
		
		return frase; //Estamos retornando o objeto "frase", que foi salvo na tabela "frs_frase".
	}
	
}
