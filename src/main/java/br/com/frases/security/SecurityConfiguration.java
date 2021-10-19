package br.com.frases.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //Essa anotação serve para inserirmos uma configuração básica de segurança da aplicação.
@EnableGlobalMethodSecurity(prePostEnabled = true) //Estamos definindo que a segurança da aplicação será realizada através de anotações.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter { //Estamos herdando a classe "WebSecurityConfigurerAdapter", que é uma classe que permite a configuração da segurança da aplicação com o Spring Security, assim, essa classe adiciona vários métodos que permitem a configuração da segurança da aplicação.

	@Autowired //Essa anotação explicita que o Spring será o responsável por utilizar o comando "new" no objeto "userDetailsService" sempre que necessário.
	private UserDetailsService userDetailsService;
	
	@Override //A anotação "@Override" serve para explicitarmos que o método abaixo está sendo herdado da classe "WebSecurityConfigurerAdapter" e sendo sobrescrito.
	protected void configure(HttpSecurity http) throws Exception { //Dentro desse método, poderemos inserir várias configurações de segurança da aplicação.
		http.csrf().disable() //Estamos desabilitando a proteção contra ataques do tipo "CSRF", ou "cross-site request forgery".
			.addFilterBefore(new JwtAuthenticationFilters(), UsernamePasswordAuthenticationFilter.class) //Esse método permite que adicionemos um filtro antes de um filtro já existente na aplicação, assim, estamos adicionando o filtro que criamos, que está dentro da classe "JwtAuthenticationFilters", antes do filtro "UsernamePasswordAuthenticationFilter", que é o filtro próprio, do Spring Security, que verificará se o usuário está logado, assim, antes de verificarmos se o usuário está logado, utilizaremos o filtro "JwtAuthenticationFilter" que logará o usuário através do token.
			.sessionManagement() //Poderemos adicionar métodos relacionados à gestão de sessões.
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Essa configuração obriga que, após uma requisição ser feita, a sessão utilizada para realizar essa requisição seja fechada. Esse é um padrão utilizado ao trabalharmos com REST.
	}
	
	@Override //A anotação "@Override" serve para explicitarmos que o método abaixo está sendo herdado da classe "WebSecurityConfigurerAdapter" e sendo sobrescrito.
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { //Esse método permite a configuração do gerenciador de autenticação da aplicação, ou seja, a configuração da forma que o login será realizado.
		auth.userDetailsService(userDetailsService); //Estamos definindo o serviço que o gerenciador de autenticação usará, que, nesse caso, é o serviço "userDetailsService".
	}
	
	@Bean //Essa anotação permite que o objeto do tipo "AuthenticationManager" que foi retornado por esse método fique disponível para ser instanciado e utilizado pelo Spring.
	@Override //A anotação "@Override" serve para explicitarmos que o método abaixo está sendo herdado da classe "WebSecurityConfigurerAdapter" e sendo sobrescrito.
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean //Essa anotação permite que o objeto do tipo "PasswordEncoder", que foi retornado por esse método, fique disponível para ser instanciado e utilizado por todo o sistema do Spring, assim, em qualquer lugar da aplicação, o Spring poderá utilizar esse encoder.
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //Esse objeto servirá para encriptar as senhas antes delas serem salvas no banco de dados.
	}
	
}
