package ro.mira.peronline.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import ro.mira.peronline.constantes.Constantes;
import ro.mira.peronline.lazydata.LazyDataUsers;
import ro.mira.peronline.persistence.entities.PLocality;
import ro.mira.peronline.persistence.entities.PProvince;
import ro.mira.peronline.persistence.entities.Users;
import ro.mira.peronline.services.CountryService;
import ro.mira.peronline.services.LocalityService;
import ro.mira.peronline.services.ProvinceService;
import ro.mira.peronline.services.UserService;
import ro.mira.peronline.util.FacesUtilities;

/**
 * Controlor de operațiuni legate de gestionarea utilizatorilor. Înregistrarea utilizatorilor, modificarea
 * utilizatorilor, ștergerea utilizatorilor, căutarea utilizatorilor, parola de căutare și restaurare.
 * 
 * @author STAD
 */

@Setter
@Getter
@Controller("userBean")
@Scope(Constantes.SESSION)
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Utilizator/Membru.
	 */
	private Users user;

	/**
	 * Objeto de búsqueda de usuario.
	 */
	private SearchUser searchUsers;

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
	private transient UserService userService;

	/**
	 * Encriptador de palabras clave.
	 */
	@Autowired
	private transient PasswordEncoder passwordEncoder;

	/**
	 * Variabila utilizata pentru a injecta serviciul provinciei.
	 * 
	 */
	@Autowired
	private transient ProvinceService provinceService;

	/**
	 * Variabila utilizata pentru a injecta serviciul tarii.
	 * 
	 */
	@Autowired
	private transient CountryService countryService;

	/**
	 * Variabila utilizata pentru a injecta serviciul localitatilor.
	 * 
	 */
	@Autowired
	private transient LocalityService localityService;

	/**
	 * Lista de localitati.
	 */
	private List<PLocality> localidades;

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
		setSearchUsers(new SearchUser());
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
	public void onChangeProvince(PProvince province) {
		if (searchUsers.getProvince() != null) {
			setLocalidades(localityService.findByProvince(province));
		}
		else {
			setLocalidades(null);
		}
	}

	/**
	 * Inicializeaza bean-ul.
	 */
	@PostConstruct
	public void init() {
		this.provinces = new ArrayList<>();
		this.localidades = new ArrayList<>();
		this.province = new PProvince();
		// setSearchUsers(model.getSearchUser());
		this.searchUsers = new SearchUser();
		setProvinces(provinceService.fiindAll());
		// pentru a se încârca în mod implicit opțiunea "Selectați una ..."

		this.list = new ArrayList<>();
		for (int i = 0; i <= numeroColumnasListadoUsarios; i++) {
			list.add(Boolean.TRUE);
		}
		this.model = new LazyDataUsers(userService);

		// Utilities.cleanSession("userBean");
	}
}