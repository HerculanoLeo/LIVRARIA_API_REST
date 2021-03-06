package br.com.herculano.livararia_api_rest.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.herculano.livararia_api_rest.controller.request.usuario.UsuarioConsultaRequest;
import br.com.herculano.livararia_api_rest.entity.Perfil;
import br.com.herculano.livararia_api_rest.entity.Usuario;
import br.com.herculano.livararia_api_rest.repository.custom.UsuarioRespositoryCustom;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRespositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Usuario> consultaPorFiltro(UsuarioConsultaRequest entity, Pageable page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> user = cq.from(Usuario.class);
		Join<Usuario, Perfil> perfil = user.join("perfil", JoinType.INNER);

		Map<String, JoinType> joinMap = new HashMap<>();
		joinMap.put("perfil", JoinType.INNER);

		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(entity.getNome())) {
			predicates.add(cb.like(cb.upper(user.get("nome")), "%" + entity.getNome().toUpperCase() + "%"));
		}

		if (StringUtils.isNotBlank(entity.getEmail())) {
			predicates.add(cb.like(cb.upper(user.get("email")), "%" + entity.getEmail().toUpperCase() + "%"));
		}

		if (StringUtils.isNotBlank(entity.getTipoUsuario())) {
			predicates.add(cb.equal(user.get("tipoUsuario"), entity.getTipoUsuario()));
		}

		if (StringUtils.isNotBlank(entity.getNomePerfil())) {
			predicates.add(cb.like(cb.upper(perfil.get("nome")), "%" + entity.getNomePerfil().toUpperCase() + "%"));
		}

		cq.select(user).where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<Usuario> query = em.createQuery(cq);

		query.setFirstResult(Math.toIntExact(page.getOffset()));
		query.setMaxResults(page.getPageSize());

		List<Usuario> entities = query.getResultList();

		return new PageImpl<Usuario>(entities, page, totalRegistros(predicates));
	}

	private Long totalRegistros(List<Predicate> predicates) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> root = cq.from(Usuario.class);

		root.join("perfil", JoinType.INNER);

		cq.select(cb.count(root));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		return em.createQuery(cq).getSingleResult();
	}

}
