package ro.per.online.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import ro.per.online.constantes.Constantes;
import ro.per.online.lazydata.LazyDataUsers;
import ro.per.online.persistence.entities.PLocality;
import ro.per.online.persistence.entities.PProvince;
import ro.per.online.persistence.entities.Users;
import ro.per.online.services.CountryService;
import ro.per.online.services.LocalityService;
import ro.per.online.services.ProvinceService;
import ro.per.online.services.UserService;
import ro.per.online.util.FacesUtilities;

/**
 * Controlor de operațiuni legate de gestionarea utilizatorilor. Înregistrarea utilizatorilor, modificarea
 * utilizatorilor, ștergerea utilizatorilor, căutarea utilizatorilor, parola de căutare și restaurare.
 * 
 * @author STAD
 */

@Setter
@Getter
@Controller("userBean")
// @Scope("session")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Ruta buscador de isnpecciones.
	 */
	private static final String RUTABUSCAUSERS = "/users/users?faces-redirect=true";

	/**
	 * Utilizator/Membru.
	 */
	private Users user;

	/**
	 * Objeto de búsqueda de usuario.
	 */
	private UsuarioBusqueda searchUsers;

	/**
	 * Lista de judete.
	 */
	private List<PProvince> provinces;

	/**
	 * Judetul
	 */
	private PProvince province;

	/**
	 * Lista de booleanos para el control de la visualización de columnas en la vista.
	 */
	private List<Boolean> list;

	/**
	 * Número máximo de columnas visibles en la vista.
	 */
	private int numeroColumnasListadoUsarios = 9;

	/**
	 * Array que contiene los niveles seleccionables.
	 */
	private int[] nivelesSelect = IntStream.rangeClosed(12, 30).toArray();

	/**
	 * LazyModel para la paginación desde servidor de los datos de la búsqueda de usuarios.
	 */
	private LazyDataUsers model;

	/**
	 * Servicio de usuarios.
	 */
	@Autowired
	private UserService userService;

	/**
	 * Encriptador de palabras clave.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Variabila utilizata pentru a injecta serviciul provinciei.
	 * 
	 */
	@Autowired
	private ProvinceService provinceService;

	/**
	 * Variabila utilizata pentru a injecta serviciul tarii.
	 * 
	 */
	@Autowired
	private CountryService countryService;

	/**
	 * Variabila utilizata pentru a injecta serviciul localitatilor.
	 * 
	 */
	@Autowired
	private LocalityService localityService;

	/**
	 * Lista de localitati.
	 */
	private List<PLocality> localidades;

	/**
	 * Variabilă folosită pentru a stoca valoarea provinciei selectate.
	 * 
	 */
	private PProvince provinciSelec;

	/**
	 * Afișează profilul utilizatorului
	 * 
	 * @return URL-ul paginii unde se vede profilul utilizatorului.
	 */
	public String getUserPerfil() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		user = userService.fiindOne(username);
		return "/principal/miPerfil?faces-redirect=true";
	}

	/**
	 * Metodă care ne duce la formularul de înregistrare a utilizatorilor noi, inițializând tot ceea ce este necesar
	 * pentru afișarea corectă a paginii (enums, utilizator nou,..etc). Seapeleaza din pagina căutare utilizator.
	 * 
	 * @return url-ul páginii de inregistrare utilizator
	 */
	public String newUser() {
		user = new Users();
		user.setDateCreate(new Date());
		return "/users/registerUser?faces-redirect=true";
	}

	/**
	 * Returnează formularul de căutare utilizator în starea sa inițială și șterge rezultatele căutărilor anterioare
	 * dacă este navigat din meniu sau dintr-o altă secțiune.
	 *
	 * @return pagina de căutare a utilizatorilor
	 */
	public String getSearchFormUsers() {
		cleanSearch();
		return "/users/users.xhtml?faces-redirect=true";
	}

	/**
	 * Șterge rezultatele căutărilor anterioare.
	 * 
	 */
	public void cleanSearch() {
		setSearchUsers(new UsuarioBusqueda());
		model.setRowCount(0);
	}

	/**
	 * Caută utilizatori în funcție de filtrele introduse în formularul de căutare.
	 */
	public void searchUsers() {
		model.setSearchUser(searchUsers);
		model.load(0, Constantes.TAMPAGINA, "dateCreate", SortOrder.DESCENDING, null);
	}

	/**
	 * Transmite datele utilizatorului pe care dorim să le modificăm în formular, astfel încât acestea să schimbe
	 * valorile pe care le doresc.
	 * 
	 * @param usuario Utilizator recuperat din formularul de căutare al utilizatorului
	 * @return URL-ul paginii de modificare a utilizatorului
	 */
	public String getFormModifyUser(final Users usuario) {
		final Users usu = userService.fiindOne(usuario.getUsername());
		String redireccion = null;
		if (usu != null) {
			this.user = usu;
			redireccion = "/users/modifyUser?faces-redirect=true";
		}
		else {
			FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_ERROR, "Modificare",
					" A apărut o eroare în căutarea utilizatorului. Utilizatorul nu există.");
		}
		return redireccion;
	}

	/**
	 * Activați / dezactivați vizibilitatea coloanelor din tabelul cu rezultate.
	 * 
	 * @param e checkbox al coloanei selectate
	 */
	public void onToggle(ToggleEvent e) {
		list.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	/**
	 * Returnează o listă a localităților care aparțin unui judet. Acesta este folosit pentru a reîncărca lista
	 * localităților în funcție de judetul selectat.
	 */
	public void onChangeProvince(PProvince provincia) {
		if (provinciSelec != null) {
			setLocalidades(localityService.findByProvince(provinciSelec));
		}
		else {
			setLocalidades(null);
		}
	}

	/**
	 * 
	 * Limpia el menú de búsqueda si se accede a través del menú lateral.
	 * @return ruta siguiente
	 * 
	 */

	public String getFormularioBusqueda() {
		setProvinces(provinceService.fiindAll());
		// provinces = provinceService.fiindAll();
		limpiarBusqueda();

		return RUTABUSCAUSERS;
	}

	/**
	 * 
	 * Limpia los valores del objeto de búsqueda de inspecciones.
	 * 
	 */

	public void limpiarBusqueda() {
		searchUsers = new UsuarioBusqueda();
		provinciSelec = new PProvince();
		model.setRowCount(0);
	}

	/**
	 * Inicializeaza bean-ul.
	 */
	@PostConstruct
	public void init() {
		// this.provinces = new ArrayList<>();
		this.localidades = new ArrayList<>();
		this.provinciSelec = new PProvince();
		// setSearchUsers(model.getSearchUser());
		this.searchUsers = new UsuarioBusqueda();

		// pentru a se încârca în mod implicit opțiunea "Selectați una ..."

		this.list = new ArrayList<>();
		for (int i = 0; i <= numeroColumnasListadoUsarios; i++) {
			list.add(Boolean.TRUE);
		}
		this.model = new LazyDataUsers(userService);

		// Utilities.cleanSession("userBean");
	}

}
