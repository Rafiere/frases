package br.com.frases.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilters extends GenericFilterBean {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest servletRequest = (HttpServletRequest) request; //Esse filtro obterá a requisição que foi feita, que será o parâmetro "request" do tipo "ServletRequest" e armazenará essa requisição no objeto "request", que é do tipo "HttpServletRequest".
			String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION); //Estamos criando o objeto "authorization", que é do tipo "String", esse objeto obterá, caso exista, o header "AUTHORIZATION" dessa requisição que está sendo realizada. Por padrão, o JWT precisa ir dentro do header "AUTHORIZATION", por isso, estamos obtendo o valor desse header.
			if(authorization != null) { //Se o header "AUTHORIZATION" não estiver vazio, isso significa que essa requisição possui um token.
				User usuario = JwtUtils. //Estamos criando o objeto "usuario", que é do tipo "User", esse objeto receberá o usuário que estava dentro do token que foi enviado através da requisição.
						parseToken(authorization.replaceAll("Bearer ", "")); //O método "parseToken()" serve para lermos esse token, assim, se o token estiver com qualquer problema, como uma assinatura inválida ou estiver expirado, uma exception será gerada nessa parte. Por isso, inserimos o "try-catch()". O "replaceAll("Bearer ", "")" serve para, caso o token venha com o prefixo "Bearer ", deveremos descartar esse prefixo, para possuirmos apenas o token. Não são todos os tokens que vêm com esse prefixo, porém, caso algum token recebido venha com esse prefixo, esse prefixo será excluído, pois não utilizaremos esse prefixo na leitura do token pelo método "parseToken()".
				Authentication credentials = new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities()); //Estamos criando o objeto "credentials", que é do tipo "Authentication", esse objeto está recebendo o retorno do método construtor da classe "UsernamePasswordAuthenticationToken", e, para esse método construtor, estamos passando o nome do usuário, a senha e as autorizações desse usuário, todas essas informações foram enviadas através do token que foi lido pelo método "parseToken()".
				SecurityContextHolder.getContext().setAuthentication(credentials); //O método "getContext()" serve para obtermos o contexto, que é a área de memória que o Spring Security utiliza para verificar se o usuário está logado e, através do método "setAuthentication()", inserirá os dados desse usuário nessa área de memória. Quando executamos esse método, o usuário será logado de forma automática.
			}
			chain.doFilter(request, response); //Após passar pelo filtro, o método "doFilter()" liberará a requisição para acessar o serviço desejado.
		}catch(Throwable t) { //Caso qualquer exception ocorra, como, por exemplo, o token enviado estiver com uma assinatura inválida ou expirado, o fluxo de execução do código será desviado para o bloco "catch()". Se ocorrer qualquer erro, esse filtro não executará o método "doFilter()", ou seja, a requisição não poderá alcançar o seu destino final, que é o serviço que ela está tentando acessar.
			HttpServletResponse servletResponse = (HttpServletResponse) response; //Se uma exception ocorrer, estamos obtendo a resposta que será enviada para essa requisição e armazenando essa resposta dentro do objeto "servletResponse", que é do tipo "HttpServletResponse".
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, t.getMessage()); //Estamos, na resposta que será enviada à requisição, enviando um erro, esse erro conterá um código de erro, informando que esse usuário não está habilitado a realizar essa requisição.
		}
	}
}
