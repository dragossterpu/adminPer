package ro.per.online.web.beans;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.per.online.constantes.Constantes;
import ro.per.online.lazydata.LazyDataUsers;
import ro.per.online.persistence.entities.PLocality;
import ro.per.online.persistence.entities.PProvince;
import ro.per.online.persistence.entities.PersonalData;
import ro.per.online.persistence.entities.Team;
import ro.per.online.persistence.entities.Users;
import ro.per.online.persistence.entities.enums.CivilStatusEnum;
import ro.per.online.persistence.entities.enums.EducationEnum;
import ro.per.online.persistence.entities.enums.RoleEnum;
import ro.per.online.persistence.entities.enums.SexEnum;
import ro.per.online.services.LocalityService;
import ro.per.online.services.ProvinceService;
import ro.per.online.services.TeamService;
import ro.per.online.services.UserService;
import ro.per.online.util.FacesUtilities;
import ro.per.online.util.Generador;

/**
 * Clase utilizada para cargar datos en la pagina de echipa PER.
 * 
 * @author STAD
 *
 */
@Component("teamBean")
@Setter
@Getter
@NoArgsConstructor
@SessionScoped
public class TeamBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Variala utilizata pentru recuperarea listei echipei de conducere.
	 * 
	 */
	private List<Team> listaTeam;

	/**
	 * Variala utilizata pentruinjectarea serviciului de team.
	 * 
	 */
	@Autowired
	private transient TeamService teamService;

	/**
	 * Lista de usuarios seleccionados.
	 */
	private List<Users> usuariosSeleccionadosFinales;

	/**
	 * Numele de utilizator care va fi folosit pentru al inregisdtra in echipa.
	 */
	private String nombreUsuario = Constantes.ESPACIO;

	/**
	 * Lazy model para las usuarios.
	 */
	private transient LazyDataUsers modelUser;

	/**
	 * Service de usuarios.
	 */
	@Autowired
	private transient UserService usuarioService;

	/**
	 * Alerta nueva.
	 */
	private transient Team team;

	/**
	 * Clase de búsqueda de usuarios.
	 */
	private UsuarioBusqueda usuarioBusqueda;

	/**
	 * Lista utilizatorilor selectați.
	 */
	private List<Users> usuariosSeleccionados;

	/**
	 * Lista judetelor.
	 */
	private List<PProvince> listaProvincias;

	/**
	 * Judetul selectațat.
	 */
	private PProvince provinciaSelect;

	/**
	 * Variabila utilizata pentru a injecta serviciul provinciei.
	 * 
	 */
	@Autowired
	private ProvinceService provinceService;

	/**
	 * Variabila utilizata pentru a injecta serviciul provinciei.
	 * 
	 */
	@Autowired
	private UserService userService;

	/**
	 * Variabila utilizata pentru a injecta serviciul provinciei.
	 * 
	 */
	@Autowired
	private LocalityService localityService;

	/**
	 * Lista numelor din echipa de conducere.
	 */
	private List<Team> listaTeams;

	/**
	 * Indică dacă doriți să căutați după datele utilizatorului (opțiunea 1).
	 */
	private Integer opcion = 1;

	/**
	 * Eliminarea unui membru al echipei.
	 * @param team membru candidat pentru eliminare
	 */
	public void eliminarMembru(final Team team) {
		try {
			teamService.delete(team);
			listaTeam.remove(team);
		}
		catch (DataAccessException e) {
			FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_ERROR, "Error",
					"A apărut o eroare la eliminarea membrului echipei de conducere, încercați din nou mai târziu");
		}
	}

	/**
	 * Deschide dialogul de căutare pentru utilizatori.
	 */
	public void abrirDialogoBusquedaUsuarios() {
		listaProvincias = provinceService.fiindAll();
		listaTeams = teamService.fiindByTeam();
		modelUser = new LazyDataUsers(usuarioService);
		final RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgBusqueda').show();");
	}

	/**
	 * Modificarea descrierii unui membru al equipei.
	 * @param event eveniment care capturează team de editat
	 */
	public void onRowEdit(final RowEditEvent event) {

		try {
			final Team team = (Team) event.getObject();
			teamService.save(team);
			FacesUtilities.setMensajeInformativo(FacesMessage.SEVERITY_INFO, "Membru al echipei de conducere modificat",
					team.getTeam().getDescription(), null);
		}
		catch (DataAccessException e) {
			FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_ERROR, Constantes.ERRORMENSAJE,
					"A apărut o eroare în încercarea de a modifica membrul echipei de conducere, încercați din nou mai târziu");
		}
	}

	/**
	 * Înregistrează utilizatorul indicat.
	 */
	public void save() {
		try {
			if (usuariosSeleccionadosFinales.isEmpty()) {
				Users usuario = usuarioService.fiindOne(nombreUsuario);
				if (usuario == null) {
					usuario = new Users();
					usuario.setUsername(nombreUsuario);
				}
				usuariosSeleccionadosFinales.add(usuario);
			}
			teamService.save(team);
			FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_INFO, " Înregistrare corectă ",
					"Membrul a fost înregistrat corect.");
			limpiarCamposNewTeam();
		}
		catch (final DataAccessException e) {
			FacesUtilities.setMensajeConfirmacionDialog(FacesMessage.SEVERITY_ERROR, Constantes.ERRORMENSAJE,
					"A apărut o eroare la înregistrarea utilizatorului, încercați din nou mai târziu.");
		}
	}

	/**
	 * Curăță câmpurile utilizatorilor selectați și lista de utilizatori.
	 */
	public void limpiarCamposNewTeam() {
		usuariosSeleccionadosFinales = new ArrayList<>();
		modelUser = new LazyDataUsers(usuarioService);
	}

	/**
	 * Căută utilizatori pe baza unui filtru.
	 * @return
	 */
	public List<Users> buscaUsuarios() {
		modelUser.setSearchUser(usuarioBusqueda);
		return modelUser.load(0, Constantes.TAMPAGINA, Constantes.FECHACREACION, SortOrder.DESCENDING, null);
	}

	/**
	 * Căută utilizatori pe baza unui filtru.
	 */
	public void buscarUsuarios() {
		modelUser.setSearchUser(usuarioBusqueda);
		modelUser.load(0, Constantes.TAMPAGINA, "dateCreate", SortOrder.DESCENDING, null);
	}

	/**
	 * Metodă care captează evenimentul "Selectați toate" sau "Deselectați toate" în vizualizarea Team.
	 * 
	 * @param toogleEvent ToggleSelectEvent
	 */
	public void onToggleSelectUsers(final ToggleSelectEvent toogleEvent) {
		if (toogleEvent.isSelected()) {
			usuariosSeleccionadosFinales = buscaUsuarios();
			modelUser.setDatasource(usuariosSeleccionadosFinales);
			usuariosSeleccionados = new ArrayList<>(usuariosSeleccionadosFinales);
		}
	}

	/**
	 * Metoda de a adăuga noi utilizatori la lista de utilizatori selectați.
	 */
	public void onChangePageUsuarios() {
		if (usuariosSeleccionados != null && !usuariosSeleccionados.isEmpty()) {
			usuariosSeleccionadosFinales.addAll(usuariosSeleccionados);
			usuariosSeleccionados = new ArrayList<>(usuariosSeleccionadosFinales);
		}
	}

	/**
	 * Metodă care asociază o inspecție când selectați caseta de selectare.
	 *
	 * @param event eveniment lansat care conține utilizatorul
	 */
	public void onRowSelectedUser(final SelectEvent event) {
		final Users i = (Users) event.getObject();
		usuariosSeleccionadosFinales.add(i);
		modelUser.setDatasource(usuariosSeleccionadosFinales);
	}

	/**
	 * Curăță căutarea utilizatorilor
	 */
	public void limpiarBuscadores() {
		usuarioBusqueda = new UsuarioBusqueda();
		usuariosSeleccionados = new ArrayList<>();
		modelUser = new LazyDataUsers(usuarioService);
	}

	/**
	 * Inițializarea datelor.
	 */
	@PostConstruct
	public void init() {
		this.usuarioBusqueda = new UsuarioBusqueda();
		this.listaTeam = new ArrayList<>();
		this.listaTeam = teamService.fiindByTeam();
		limpiarBuscadores();
	}

	public void alta() {
		for (int i = 0; i < 100; i++) {
			Users user = new Users();
			user.setDateCreate(Generador.ObtenerFecha());
			user.setEmail("proba@gmail.com");
			user.setLastName(Generador.nombreFinal());
			user.setName(Generador.apellidoFinal());
			user.setPassword("$2a$10$tDGyXBpEASeXlAUCdKsZ9u3MBBvT48xjA.v0lrDuRWlSZ6yfNsLve");
			PersonalData pd = new PersonalData();
			pd.setAddress(Generador.nombresCalleFinal().concat("Nr :").concat(Generador.getNumeroCalle()));
			pd.setBirthDate(Generador.ObtenerFecha());
			pd.setCivilStatus(CivilStatusEnum.MARRIED);
			pd.setEducation(EducationEnum.BASIC);
			pd.setIdCard(Generador.getUnidadNumber());
			PProvince pro = new PProvince();
			pro.setId(Generador.provinciasFinal());
			pd.setProvince(pro);
			List<PLocality> loc = new ArrayList<>();
			loc = localityService.findByProvince(pro);
			PLocality locality = new PLocality();
			Random rand = new Random();
			locality = loc.get(rand.nextInt(loc.size()));
			pd.setLocality(locality);
			pd.setNumberCard(Generador.getDni());
			pd.setPersonalEmail(mail(user.getName(), user.getLastName()));
			pd.setPhone(Generador.getTelefon());
			pd.setSex(SexEnum.UNSPECIFIED);
			pd.setValidated(true);
			pd.setWorkplace("Nespecificat");
			user.setPersonalData(pd);
			user.setUsername(pd.getPersonalEmail());
			user.setRole(RoleEnum.ROLE_MEMBER);
			userService.save(user);
		}
	}

	/**
	 * Obtener DNI.
	 * @param prenume
	 * @param nume
	 * @return dni + letra
	 */
	public static String mail(String nume, String prenume) {
		String email = null;
		String limpioName = Normalizer.normalize(nume.toLowerCase(), Normalizer.Form.NFD);
		limpioName.replaceAll("\\s", "");
		String limpioPrenume = Normalizer.normalize(prenume.toLowerCase(), Normalizer.Form.NFD);
		limpioPrenume.replaceAll("\\s", "");
		return limpioName.concat(".").concat(limpioPrenume.concat(Generador.nombresMail()));
	}

}
