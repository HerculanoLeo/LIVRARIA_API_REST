package br.com.herculano.livararia_api_rest.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.herculano.livararia_api_rest.repository.custom.GrupoRepositoryCustom;

@Repository
public class GrupoRepositoryImpl implements GrupoRepositoryCustom{
	
	@PersistenceContext
	private EntityManager em;
}