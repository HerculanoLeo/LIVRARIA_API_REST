package br.com.herculano.livararia_api_rest.controller.response;

import br.com.herculano.livararia_api_rest.entity.Biblioteca;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BibliotecaResponse {

	private Integer id;
	
	private String nome;
	
	private UsuarioResponse usuarioAdministrador;
	
	public BibliotecaResponse(Biblioteca entity, UsuarioResponse usuarioResponse) {
		this.id = entity.getId();
//		this.nome = entity.getNome();
		this.usuarioAdministrador = usuarioResponse;
	}

	public BibliotecaResponse(Biblioteca entity) {
		this.id = entity.getId();
//		this.nome = entity.getNome();
//		this.usuarioAdministrador = new UsuarioResponse(entity);
	}
	
}