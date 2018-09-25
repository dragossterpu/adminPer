package ro.per.online.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.per.online.constantes.Constantes;
import ro.per.online.persistence.entities.Users;
import ro.per.online.persistence.repositories.ProvinceRepository;
import ro.per.online.persistence.repositories.UserRepository;
import ro.per.online.services.UserService;
import ro.per.online.util.FacesUtilities;
import ro.per.online.util.UtilitiesCriteria;
import ro.per.online.web.beans.UsuarioBusqueda;

/**
 * Implementación del servicio de unidades.
 *
 * @author STAD
 *
 */

@Transactional
@Service
public class UserServiceImpl implements UserService {

	/**
	 * Repositoriu de Users.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Repositoriu de PProvince.
	 */
	@Autowired
	private ProvinceRepository provinceRepository;

	/**
	 * SessionFactory.
	 */
	@Autowired
	private transient SessionFactory sessionFactory;

	/**
	 * Session.
	 */

	private Session session;

	/**
	 * Devuelve toti utilizatorii inregistrati in baza de date.
	 * @return lista de unidades
	 */
	@Override
	public List<Users> fiindAll() {
		final Iterable<Users> users = userRepository.findAll();
		return (List<Users>) users;
	}

	/**
	 * Devuelve utilizatorul inregistrat in baza de date.
	 * @return Users user
	 * @see
	 */
	@Override
	public Users fiindOne(String id) {
		final Users user = userRepository.findOne(id);
		return user;
	}

	/**
	 * Busca usuarios con los parametros de búsqueda.
	 * @param usuarioBusqueda UsuarioBusqueda
	 * @param sortOrder SortOrder
	 * @param sortField String
	 * @param pageSize int
	 * @param first int
	 *
	 * @return List<User>
	 *
	 *
	 */
	@Override
	public List<Users> buscarUsuarioCriteria(final int first, final int pageSize, final String sortField,
			final SortOrder sortOrder, final UsuarioBusqueda usuarioBusqueda) {
		try {
			this.session = this.sessionFactory.openSession();
			final Criteria criteria = this.session.createCriteria(Users.class, "user");
			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);
			if (sortField != null) {
				if (sortOrder.equals(SortOrder.ASCENDING)) {
					criteria.addOrder(Order.asc(sortField));
				}
				else if (sortOrder.equals(SortOrder.DESCENDING)) {
					criteria.addOrder(Order.desc(sortField));
				}
			}
			else {
				criteria.addOrder(Order.desc("dateCreate"));
			}
			List<Users> usuariosList;
			creaCriteria(usuarioBusqueda, criteria);

			usuariosList = criteria.list();
			this.session.close();
			return usuariosList;
		}
		finally {
			closeSession();
		}
	}

	/**
	 * @param usuarioBusqueda
	 * @param criteria
	 */
	private void creaCriteria(final UsuarioBusqueda usuarioBusqueda, final Criteria criteria) {
		List<Users> usuariosList = new ArrayList<>();
		UtilitiesCriteria.setCondicionCriteriaFechaMayor(usuarioBusqueda.getDateFrom(), criteria,
				Constantes.FECHACREACION);
		UtilitiesCriteria.setCondicionCriteriaFechaMenorIgual(usuarioBusqueda.getDateUntil(), criteria,
				Constantes.FECHACREACION);
		UtilitiesCriteria.setCondicionCriteriaCadenaLike(usuarioBusqueda.getName(), criteria, "name");
		UtilitiesCriteria.setCondicionCriteriaCadenaLike(usuarioBusqueda.getLastName(), criteria, "lastName");
		UtilitiesCriteria.setCondicionCriteriaIgualdadLong(usuarioBusqueda.getId(), criteria,
				"personalData.province.id");
	}

	/**
	 * Manejo y cierre de la sesión.
	 */
	private void closeSession() {
		if (this.session != null && this.session.isOpen()) {
			try {
				this.session.close();
			}
			catch (final DataAccessException e) {
				FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_ERROR, Constantes.ERRORMENSAJE,
						"Error mesaj");
			}
		}
	}

	/**
	 * Obtiene el conteo de criteria.
	 * @param busqueda UsuarioBusqueda
	 * @return int
	 */
	@Override
	public int getCounCriteria(final UsuarioBusqueda busqueda) {
		try {
			this.session = this.sessionFactory.openSession();
			final Criteria teria = this.session.createCriteria(Users.class, "user");
			creaCriteria(busqueda, teria);
			teria.setProjection(Projections.rowCount());
			final Long cnt = (Long) teria.uniqueResult();

			return Math.toIntExact(cnt);
		}
		finally {
			closeSession();
		}

	}

	/*
	 * Metoda care genereaza automat 100 de utilizatori
	 * 
	 * @see ro.per.online.services.UserService#save(ro.per.online.persistence.entities.Users)
	 */
	@Override
	@Transactional(readOnly = false)
	public Users save(Users entity) {
		return userRepository.save(entity);
	}
}
