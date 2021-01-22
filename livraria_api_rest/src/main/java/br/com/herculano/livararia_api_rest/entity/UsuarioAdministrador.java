package br.com.herculano.livararia_api_rest.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_usuario_adminisitrador")
@PrimaryKeyJoinColumn(name = "id_usuario", referencedColumnName = "id")
public class UsuarioAdministrador extends Usuario {

	private static final long serialVersionUID = -423970627818472626L;
	
	@Column(name = "documento", nullable = false)
	private String documento;
	
	@Transient
	private List<Biblioteca> bibliotecas;

}