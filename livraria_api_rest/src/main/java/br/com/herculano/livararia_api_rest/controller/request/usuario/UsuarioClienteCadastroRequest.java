package br.com.herculano.livararia_api_rest.controller.request.usuario;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.herculano.livararia_api_rest.entity.Perfil;
import lombok.Data;

@Data
public class UsuarioClienteCadastroRequest {

	@NotBlank
	private String nome;
	
	@NotBlank
	private String documento;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String senha;
	
	@NotBlank
	private String confirmeSenha;

	@NotBlank
	private String idioma;
	
	@JsonIgnore
	private String tipo;
	
	@JsonIgnore
	private Perfil perfil;
	
	public UsuarioClienteCadastroRequest(String nome, String email, String senha, String confirmeSenha, String idioma) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.confirmeSenha = confirmeSenha;
		this.idioma = idioma;
	}
	
}
