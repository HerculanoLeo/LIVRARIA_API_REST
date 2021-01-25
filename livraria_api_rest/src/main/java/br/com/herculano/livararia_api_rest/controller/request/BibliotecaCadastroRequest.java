package br.com.herculano.livararia_api_rest.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BibliotecaCadastroRequest {
	
	@NotNull
	private Integer idAdministrador;
	
	@NotBlank
	private String nome;

}
