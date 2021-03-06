package br.com.herculano.livararia_api_rest.repository.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.herculano.livararia_api_rest.entity.Usuario;

@Component
public class ContextUtils {

	public static Usuario getUsuarioAutenticado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
