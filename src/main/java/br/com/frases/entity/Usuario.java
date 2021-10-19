package br.com.frases.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.frases.controller.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Anotação que explicita para o Lombok que ele deve gerar os métodos "getters", "setters", "hashCode()", "equals()", os métodos construtores e o método "toString()".
@AllArgsConstructor //Anotação que explicita para o Lombok que ele deve gerar um método construtor com todos os atributos.
@NoArgsConstructor //Anotação que explicita para o Lombok que ele deve gerar um método construtor com nenhum atributo, essa anotação é necessária se quisermos inserir um método construtor sem nenhum atributo e tivermos inserido a anotação "@AllArgsArguments".

@Entity //Essa anotação especifica que essa classe é uma entidade, ou seja, que essa classe mapeará uma tabela no banco de dados. 
@Table(name="usr_usuario") //Como o nome da classe é "Usuario" mas essa classe representa a tabela "usr_usuario" no banco de dados, deveremos inserir a anotação "@Table" com o atributo "name = "usr_usuario"", para explicitarmos que a classe "Usuario" está mapeando a tabela "usr_usuario" do banco de dados.
public class Usuario {

	//Colunas simples
	
	@Id //A anotação "@Id" explicita que esse atributo representará a chave primária da tabela que está sendo mapeada por essa classe.
	@GeneratedValue(strategy = GenerationType.IDENTITY) //A anotação "@GeneratedValue" com o atributo "strategy = GenerationType.IDENTITY" explicita que o MySQL será o responsável por definir os valores de cada chave primária de forma automática.
	@Column(name = "usr_id") //A anotação "@Column" serve para indicarmos como essa coluna será, assim, estamos utilizando o atributo "name" com o valor "usr_id" para explicitarmos que o atributo "id" representará a coluna "usr_id" na tabela "usr_usuario".
	private Long id;
	
	@Column(name = "usr_nome", unique=true, length = 100, nullable = false) //O atributo "name" com o valor "usr_nome" serve para indicarmos que o atributo "nome" representará a coluna "usr_nome" da tabela "usr_usuario", o atributo "unique" com o valor "true" representa que não poderão existir dois valores iguais nessa coluna, o atributo "length" com o valor "100" explicita que o tamanho máximo de um valor que será armazenado nessa coluna será de 100 caracteres, e o atributo "nullable" com o valor "false" indica que não poderemos deixar essa coluna sem nenhum valor.
	@JsonView(View.FraseCompleta.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Usuario" for convertido para o formato JSON, ele exibirá o atributo "nome" apenas se o label "FraseCompleta" for utilizado.
	private String nome;
	
	@Column(name = "usr_email", unique = true, length = 100, nullable = false) //A anotação "@Column" e os seus atributos já foram explicados anteriormente.
	@JsonView(View.FraseCompleta.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Usuario" for convertido para o formato JSON, ele exibirá o atributo "email" apenas se o label "FraseCompleta" for utilizado.
	private String email;
	
	@Column(name = "usr_senha", unique = true, length = 100, nullable = false) //A anotação "@Column" e os seus atributos já foram explicados anteriormente.
	private String senha;
	
	//Relacionamentos / Associações
	
	@ManyToMany(fetch = FetchType.LAZY) //A anotação "@ManyToMany" indica que um relacionamento "muitos para muitos" ocorrerá, e o atributo "fetch" com o valor "FetchType.LAZY" indica que os objetos do tipo "autorizacao" serão carregados apenas quando forem solicitados, isso economiza processamento.
	@JoinTable(name = "uau_usuario_autorizacao", //A anotação "@JoinTable" serve para mapearmos as associações, ela será a anotação responsável por dizer como que a tabela, que será gerada pelo relacionamento "@ManyToMany" entre as tabelas "usr_usuario" e "aut_autorizacao" será. O atributo "name" com o valor "uau_usuario_autorizacao" serve para informarmos o nome da tabela que será criada por esse relacionamento.
	joinColumns = {@JoinColumn(name = "usr_id")}, //O atributo "joinColumns" serve para inserirmos as colunas que estão na tabela "Usuario" e que farão parte da tabela "uau_usuario_autorizacao", que, nesse caso, será apenas a coluna "usr_id", estamos utilizando o "@JoinColumn" pois essa coluna será uma chave estrangeira na coluna "uau_usuario_autorizacao".
	inverseJoinColumns = {@JoinColumn(name = "aut_id")}) //O atributo "inverseJoinColumn" serve para inserirmos as colunas que estão na tabela "aut_autorizacao", que é a outra tabela que fará parte desse relacionamento, que fará parte da tabela "uau_usuario_autorizacao", como apenas a coluna "aut_id" fará parte, estamos inserindo essa coluna como uma chave estrangeira da coluna "uau_usuario_autorizacao".
	private Set<Autorizacao> autorizacoes; //Utilizamos o "Set<Autorizacao>" pois cada usuário pode ter mais de um tipo de autorização, assim, precisaremos de um "Set" para armazenarmos as autorizações de cada usuário.
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "autor") //A anotação "@OneToMany" serve para especificar que está sendo criado um relacionamento "um para muitos". A funcionalidade do atributo "fetch" já foi explicado anteriormente, e o atributo "mappedBy" serve para indicarmos qual será o lado não dominante nessa relação, assim, como o valor dessa propriedade é "autor", a tabela que tiver a coluna "autor" será a tabela não dominante nesse relacionamento, assim, como a tabela "frs_frase" possui a coluna "autor", a tabela "frs_frase" não será a tabela dominante desse relacionamento, e a tabela "usr_usuario" será a tabela dominante desse relacionamento.
	private Set<Frase> frases; //Utilizamos o "Set<Frase>" pois cada usuário pode ter mais de uma frase, dessa forma, utilizamos o "Set" para armazenarmos as frases de cada usuário.
	
}
