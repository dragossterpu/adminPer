package ro.per.online.lazydata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import lombok.Getter;
import lombok.Setter;
import ro.per.online.persistence.entities.Users;
import ro.per.online.services.UserService;
import ro.per.online.web.beans.UsuarioBusqueda;

/**
 * 
 * Model pentru paginarea de pe server care extinde modelul LazyDataModel al Primefaces.
 * @author STAD
 *
 */

@Setter
@Getter
public class LazyDataUsers extends LazyDataModel<Users> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Obiect care conține parametrii de căutare.
	 */
	private transient UsuarioBusqueda searchUser;

	/**
	 * Listă care servește modelul ca sursă de date.
	 */
	private transient List<Users> datasource;

	/**
	 * Serviciu a usar.
	 */
	private transient UserService userService;

	/**
	 * 
	 * Constructor al modelului care primește serviciul ca parametru.
	 * 
	 * @param servicio Servicio a emplear
	 */
	public LazyDataUsers(final UserService servicio) {
		userService = servicio;
	}

	/**
	 * Suprascrierea metodei getRowData pentru a lucra cu obiectele utilizatorului.
	 * 
	 * @param rowKeyCheia rândului pe care a fost vizionat vizualizarea
	 * @return Utilizator care corespunde cheii primite de parametru
	 * 
	 */

	@Override
	public Users getRowData(final String rowKey) {
		Users us = null;
		@SuppressWarnings("unchecked")
		final List<Users> listaWrapped = (List<Users>) this.getWrappedData();
		final Set<Users> setUsers = new HashSet<>();
		if (listaWrapped != null) {
			setUsers.addAll(listaWrapped);
		}
		if (getDatasource() != null) {
			setUsers.addAll(getDatasource());
		}
		Iterator<Users> iteratorUsuarios = setUsers.iterator();
		boolean encontrado = false;
		while (iteratorUsuarios.hasNext() && !encontrado) {
			Users usuario = iteratorUsuarios.next();
			if (rowKey.equals(usuario.getUsername())) {
				us = usuario;
				encontrado = true;
			}
		}
		return us;
	}

	/**
	 * Sobrescritura del método getRowKey.
	 * 
	 * @param user Objeto del que se desea obtener la clave
	 * @return Clave del objeto pasado como parámetro
	 */

	@Override
	public Object getRowKey(final Users user) {
		return user.getUsername();
	}

	/**
	 * Suprascrieți metoda de încărcare pentru a lucra cu un criteriu și returnați numai numărul de utilizatori
	 * solicitați.
	 * 
	 * @param first primul element pe care doriți să îl recuperați
	 * @param pageSize numărul maxim de înregistrări pe care dorim să le preluăm pe pagină
	 * @param sortField columna por la que se ordenarán los resultados. Corresponde a la propiedad 'sortBy' de la
	 * columna de la vista
	 * @param sortOrder orden por el que se desea ordenar los resultados
	 * @param filters mapa de filtros. Este valor no se utiliza en esta sobreescritura.
	 * @return lista de usuarios que corresponden a los criterios de búsqueda
	 * 
	 */

	@Override
	public List<Users> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
			final Map<String, Object> filters) {
		List<Users> listado = null;
		if (searchUser == null) {
			this.setRowCount(0);
		}
		else {

			this.setRowCount(userService.getCounCriteria(searchUser));
			listado = userService.buscarUsuarioCriteria(first, pageSize, sortField, sortOrder, searchUser);
		}
		return listado;

	}

}
