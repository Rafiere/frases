package br.com.frases.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "frs_frase") //Como o nome da classe é "Frase" mas essa classe representa a tabela "frs_frase" no banco de dados, deveremos inserir a anotação "@Table" com o atributo "name = "frs_frase"", para explicitarmos que a classe "Frase" está mapeando a tabela "frs_frase" do banco de dados.
public class Frase {

	//Colunas simples:
	
	@Id //A anotação "@Id" explicita que esse atributo representará a chave primária da tabela que está sendo mapeada por essa classe.
	@GeneratedValue(strategy = GenerationType.IDENTITY) //A anotação "@GeneratedValue" com o atributo "strategy = GenerationType.IDENTITY" explicita que o MySQL será o responsável por definir os valores de cada chave primária de forma automática.
	@Column(name = "frs_id")
	@JsonView(View.FraseCompleta.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Frase" for convertido para o formato JSON, ele exibirá o atributo "id" apenas se o label "FraseCompleta" for utilizado.
	private Long id;
	
	@Column(name = "frs_titulo", length = 100, unique = true, nullable = false) //Essa anotação indica que estamos criando a coluna "frs_titulo". O atributo "length" com o valor "100" indica que o tamanho máximo de caracteres que poderão ser inseridos em um campo dessa coluna é de 100 caracteres. O atributo "unique" com o valor "true" indica que não poderemos ter dois campos com valores iguais nessa coluna. O atributo "nullable" com o valor "false" indica que, obrigatoriamente, essa coluna não pode deixar de ser preenchida.
	@JsonView(View.FraseResumo.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Frase" for convertido para o formato JSON, ele exibirá o atributo "titulo" apenas se o label "FraseCompleta" ou o label "FraseResumo" forem utilizados.
	private String titulo;
	
	@Column(name = "frs_conteudo", length = 100, unique = true, nullable = false) //Essa anotação indica que estamos criando a coluna "frs_conteudo". O atributo "length" com o valor "100" indica que o tamanho máximo de caracteres que poderão ser inseridos em um campo dessa coluna é de 100 caracteres. O atributo "unique" com o valor "true" indica que não poderemos ter dois campos com valores iguais nessa coluna. O atributo "nullable" com o valor "false" indica que, obrigatoriamente, essa coluna não pode deixar de ser preenchida.
	@JsonView(View.FraseResumo.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Frase" for convertido para o formato JSON, ele exibirá o atributo "conteudo" apenas se o label "FraseCompleta" ou o label "FraseResumo" forem utilizados.
	private String conteudo;
	
	@Column(name = "frs_data_hora", nullable = false) //Essa anotação indica que esse mapeamento está relacionado à coluna "frs_data_hora". O atributo "nullable" com o valor "false" indica que essa coluna deve, obrigatoriamente, ser preenchida, ou seja, ela não pode ser deixada sem preenchimento.
	@JsonView(View.FraseCompleta.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Frase" for convertido para o formato JSON, ele exibirá o atributo "dataHora" apenas se o label "FraseCompleta" for utilizado.
	private Date dataHora;
	
	//Chave estrangeira:
	
	@ManyToOne(fetch = FetchType.EAGER) //A anotação "@ManyToOne" explicita que estamos realizando um relacionamento "muitos para um", ou seja, "uma frase pode possuir apenas um autor e um autor pode possuir várias frases". O atributo "fetch" com o valor "FetchType.EAGER" explicita que, toda vez que uma frase for buscada, o autor dessa frase será buscado também, ou seja, serão feitos dois "select".
	@JoinColumn(name = "usr_autor_id") //A anotação "@JoinColumn" deve ser utilizada para mapearmos essa coluna como uma chave estrangeira. Essa chave estrangeira será do tipo "Usuario", ou seja, ela armazenará o objeto do tipo "Usuario" que foi o autor da frase. No atributo "name", deveremos inserir o nome da coluna que possui a chave estrangeira, que, nesse caso, é a coluna "usr_autor_id".
	@JsonView(View.FraseCompleta.class) //Essa anotação serve para explicitarmos que, quando um objeto do tipo "Frase" for convertido para o formato JSON, ele exibirá o atributo "autor" apenas se o label "FraseCompleta" for utilizado.
	private Usuario autor;
}
