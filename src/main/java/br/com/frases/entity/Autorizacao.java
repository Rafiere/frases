package br.com.frases.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Anotação que explicita para o Lombok que ele deve gerar os métodos "getters", "setters", "hashCode()", "equals()", os métodos construtores e o método "toString()".
@AllArgsConstructor //Anotação que explicita para o Lombok que ele deve gerar um método construtor com todos os atributos.
@NoArgsConstructor //Anotação que explicita para o Lombok que ele deve gerar um método construtor com nenhum atributo, essa anotação é necessária se quisermos inserir um método construtor sem nenhum atributo e tivermos inserido a anotação "@AllArgsArguments".

@Entity //Essa anotação especifica que essa classe é uma entidade, ou seja, que essa classe mapeará uma tabela no banco de dados. 
@Table(name = "aut_autorizacao") //Como o nome da classe é "Autorizacao" mas essa classe representa a tabela "aut_autorizacao" no banco de dados, deveremos inserir a anotação "@Table" com o atributo "name = "aut_autorizacao"", para explicitarmos que a classe "Autorizacao" está mapeando a tabela "aut_autorizacao" do banco de dados.
public class Autorizacao {

	@Id //A anotação "@Id" explicita que esse atributo representará a chave primária da tabela que está sendo mapeada por essa classe.
	@GeneratedValue(strategy = GenerationType.IDENTITY) //A anotação "@GeneratedValue" com o atributo "strategy = GenerationType.IDENTITY" explicita que o MySQL será o responsável por definir os valores de cada chave primária de forma automática.
	@Column(name = "aut_id") //Essa anotação explicita que o atributo "id" corresponderá à coluna "aut_id" na tabela "aut_autorizacao".
	private Long id;
	
	@Column(name = "aut_nome", length = 100, unique = true, nullable = false) //Essa anotação explicita que o atributo "nome" corresponderá à coluna "aut_nome" na tabela "aut_autorizacao". O atributo "length" com o valor "100" explicita que o valor máximo de caracteres que poderão ser inseridos em um campo dessa coluna são 100 caracteres. O atributo "unique" com o valor "true" explicita que, nessa coluna, não poderá existir dois campos com o mesmo valor. O atributo "nullable" com o valor "false" explicita que não poderemos deixar nenhum campo dessa coluna sem valor, ou seja, com o valor "null".
	private String nome;
	
}
