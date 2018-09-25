package ro.per.online.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Métodos de utilidades.
 * @author STAD
 */
public class Generador {
	/**
	 * Evitamos que se pueda instanciar la clase.
	 */
	private Generador() {
		throw new IllegalAccessError("Generador class");
	}

	/**
	 * Obtiene un nombre final de la lista.
	 * @return nombre al azar
	 */
	public static String nombreFinal() {
		final String[] nombres = { "Mihai", "Ion", "Vasile", "Marius Andrei", "Grigore", "Adrian", "Iulian", "Marian",
				"Nicolae", "Gabriel", "Eugen", "Cristi", "Cristian", "Tudor", "Bogdan", "Ionel", "Claudiu", "Ovidiu",
				"Dan", "Maria", "Cristina", "Viorica", "Iulia", "Ileana", "Tudora", "Ana", "Cornel", "Marius",
				"Dan Andrei", "Tudor Cristial", "Olga", "Olga Maria", "Adela", "Maria Adela", "Luis", "David",
				"Ioan David", "Gina", "Georgeta", "Gheorghe", "Constantin", "Doru", "Dacian", "Daciana", "Manuel",
				"Benjamin", "Dana Maria", "Anghel", "Nicusor", "Tudor Adrian", "Maria Isabel" };
		return nombres[1 + new Random().nextInt(50)];

	}

	/**
	 * Obtiene un apellido final de la lista.
	 * @return apellido al azar
	 */
	public static String apellidoFinal() {
		final String[] apellidos = { "Niță", "Pop", "Popa", "Popescu", "Ionescu", "Nemeș", "Stan", "Dumitrescu", "Dima",
				"Gheorghiu", "Ioniță", "Marin", "Tudor", "Dobre", "Barbu", "Nistor", "Florea", "Ene", "Dinu",
				"Georgescu", "Stoica", "Diaconu", "Diaconescu", "Mazilescu", "Ababei", "Aanei", "Nistor", "Mocanu",
				"Oprea", "Voinea", "Dochioiu", "Albu", "Tabacu", "Manole", "Cristea", "Toma", "Stănescu", "Pușcașu",
				"Tomescu", "Meceanu", "Ionescu", "Marinescu", "Georgescu", "Vasilescu", "Stoican", "Maican",
				"Gheorghiu", "Vasiliu", "Pâclea", "Pecaru", "Stoicescu", "Tomescu", "Reaboiu", "Rîciu", "Roateș",
				"Scafariu", "Sebeșanu", "Șdirea", "Șabac", "Theodorescu" };
		String apellido = apellidos[1 + new Random().nextInt(59)];
		return apellido.toUpperCase();
	}

	/**
	 * Obtiene un pais final de la lista.
	 * @return pais al azar
	 */
	public static String paisFinal() {
		final String[] paises = { "ESPAÑA", "ALEMANIA", "RUSIA", "INGLATERA", "ESPAÑA", "ESPAÑA", "INGLATERA", "RUSIA",
				"ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA",
				"ESPAÑA", "ALEMANIA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA", "ESPAÑA" };

		return paises[1 + new Random().nextInt(25)];
	}

	/**
	 * Obtiene una provincia final de la lista.
	 * @return provincia al azar
	 */
	public static Long provinciasFinal() {
		final Long[] provincias = { 10L, 2L, 17L, 3L, 10L, 8L, 10L, 11L, 3L, 6L, 7L, 10L, 10L, 11L, 8L, 5L, 8L, 6L, 9L,
				10L, 1L, 4L, 4L, 1L, 12L, 2L, 13L, 5L, 14L, 15L, 10L, 33L, 34L, 10L, 35L, 36L, 37L, 19L, 38L, 39L, 10L,
				16L, 17L, 18L, 10L, 19L, 20L, 10L, 21L, 22L, 23L, 24L, 10L, 25L, 26L, 27L, 10L, 28L, 29L, 30L, 10L, 32L,
				31L };
		final Long provincia = provincias[1 + new Random().nextInt(59)];
		return provincia;
	}

	/**
	 * Obtiene un municipio de la lista final.
	 * @return municipio al azar
	 */
	public static String municipiosFinal() {
		final String[] municipios = { "Abades", "Alcalá de Henares", "Alcalá de Ebro", "Algueña", "Alhama de Almería",
				"Barcones", "Barjas", "Belmonte", "Cabezas del Pozo", "Cabezuela", "Cabizuela", "Cabra de Mora",
				"Cañada", "Elche", "Ferrol", "Gines", "Godall", "Gormaz", "La Galera", "La Gineta", "Barcelona",
				"Sevilla", "Barcelona", "Sevilla", "Barcelona", "Sevilla", "Madrid", "Lugo", "Lugo", "Barcelona",
				"Madrid" };
		final String municipio = municipios[1 + new Random().nextInt(30)];
		return municipio.toUpperCase();
	}

	/**
	 * Obtiene un tipo de via final de la lista.
	 * @return tipo de vía al azar
	 */
	public static String tiposViaFinal() {
		final String[] tipos = { "ARRY", "AUTOV", "AVDA", "CL", "CL", "CM", "AVDA", "AVDA", "CL", "CL", "AVDA", "CL",
				"CL", "CL", "CM", "ARRY", "CM", "CL", "AVDA", "CL", "AVDA", "AVDA", "CL", "CM", "CL", "CL", "CL", "CL",
				"AVDA", "CL", "CL" };
		return tipos[1 + new Random().nextInt(30)];
	}

	/**
	 * Obtiene un nombre de calle final de la lista.
	 * @return nombre de calle al azar
	 */
	public static String nombresCalleFinal() {
		final String[] nombresCalle = { "Strada 1 Mai nr 34", "Aleea Barajul Dunarii nr 32", "Aleea Barbatesti nr 121",
				"Intrarea Baritonului", "Soseaua Bucuresti Ploiesti", "Strada Bachus", "Strada Baia Sprie",
				"Aleea Codrii Neamtului", "Bulevardul Camil Ressu", "Bulevardul Corneliu Coposu",
				"Intrarea Caramidariei", "Strada C A Rosetti", "Bulevardul Dimitrie Pompeiu",
				"Bulevardul Dimitrie Cantemir", "Bulevardul Dinicu Golescu", "Aleea Giurgeni", "Drumul Gazarului",
				"Bulevardul Ghica Tei", "Strada Galautas", "Strada Gazelei", "Bulevardul Ion Mihalache",
				"Strada Iacob Andrei", "Strada Iezeru", "Strada Ilioara", "Aleea Piatra Mare", "Bulevardul Poligrafiei",
				"Bulevardul Protopopescu Pache", "Bulevardul Schitu Magureanu", "Aleea Stanciu George Cristian",
				"Intrarea Scorusului", "Intrarea Securii" };
		final String nombreCalle = nombresCalle[1 + new Random().nextInt(29)];
		return nombreCalle.toUpperCase();
	}

	/**
	 * Subtipos de denuncia.
	 */
	public static String nombresMail() {
		final String[] mails = { "@gmail.com", "@hotmail.com", "@yahoo.com" };
		final String mail = mails[1 + new Random().nextInt(2)];
		return mail;

	}

	/**
	 * Números.
	 */
	public static final String NUMBERS = "0123456789";

	/**
	 * Números de cuerpos.
	 */
	public static final String CUERPONUMBERS = "12789";

	/**
	 * Tipo de denuncia.
	 */
	public static final String TIPODENUNCIA = "456";

	/**
	 * Subtipos de denuncia.
	 */
	public static final String[] SUBTIPOS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

	/**
	 * Números 2.
	 */
	public static final String NUMBERS2 = "12";

	/**
	 * Letras.
	 */
	public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Obtener DNI.
	 * @return dni + letra
	 */
	public static String getDni() {
		return getPinLetters() + " " + getPinNumber();
	}

	/**
	 * Obtener unidad.
	 * @return unidad + SIAD1
	 */
	public static String getUnidad() {
		return getUnidadNumber() + "SIAD1";
	}

	/**
	 * Obtener letra de pin.
	 * @return letra aleatoria.
	 */
	public static String getPinLetters() {
		return getRandomChars(LETTERS, 2);
	}

	/**
	 * Obtener número de unidad.
	 * @return número unidad aleatorio
	 */
	public static String getUnidadNumber() {
		return getRandomChars(NUMBERS, 13);
	}

	/**
	 * Obtiene tipo de denuncia.
	 * @return tipo de denuncia aleatoria.
	 */
	public static String getTipoDenunciaNumber() {
		return getRandomChars(TIPODENUNCIA, 1);
	}

	/**
	 * Obtiene un subtipo de denuncia al azar.
	 * @return subtipo de denuncia
	 */
	public static String getSubtipoDenunciaNumber() {
		return SUBTIPOS[1 + new Random().nextInt(9)];
	}

	/**
	 * Obtiene el número de cuerpo.
	 * @return numero cuerpo
	 */
	public static String getCuerpoNumber() {
		return getRandomChars(CUERPONUMBERS, 1);
	}

	/**
	 * Obtiene el número de cuerpo.
	 * @return numero cuerpo
	 */
	public static String getTelefon() {
		return getRandomChars(NUMBERS, 9);
	}

	/**
	 * Obtiene numero id schengen.
	 * @return número aleatorio
	 */
	public static String getNumberIdShen() {
		return getRandomChars(NUMBERS, 10);
	}

	/**
	 * Obtiene el número pin.
	 * @return número aleatorio
	 */
	public static String getPinNumber() {
		return getRandomChars(NUMBERS, 4);
	}

	/**
	 * Obtiene el número de calle.
	 * @return número de calle
	 */
	public static String getNumeroCalle() {
		return getRandomChars(NUMBERS, 2);
	}

	/**
	 * Obtiene el código postal.
	 * @return codigo postal aleatorio
	 */
	public static String getCodigoPostal() {
		return getRandomChars(NUMBERS, 5);
	}

	/**
	 * Obtiene número de alias.
	 * @return numero aleatorio
	 */
	public static String getNumeroAlias() {
		return getRandomChars(NUMBERS, 4);
	}

	/**
	 * Obtiene número de identidad.
	 * @return número aleatorio
	 */
	public static String getIdentidad() {
		return getRandomChars(NUMBERS2, 1);
	}

	/**
	 * Obtiene orden de búsqueda.
	 * @return numero aleatorio
	 */
	public static String getOrdenBus() {
		return getRandomChars(NUMBERS, 3);
	}

	/**
	 * Obtiene letras aleatorias.
	 * @param key clave
	 * @param length tamaño
	 * @return Cadena de caracteres
	 */
	public static String getRandomChars(final String key, final int length) {
		final StringBuilder numeroCalle = new StringBuilder();
		final Random r = new Random();

		for (int i = 0; i < length; i++) {
			numeroCalle.append(key.charAt(r.nextInt(key.length())));
		}
		return numeroCalle.toString();
	}

	/**
	 * Obtiene una fecha aleatoria.
	 * @return fecha
	 */
	public static Date ObtenerFecha() {
		Calendar fecha;
		Random aleatorio;
		aleatorio = new Random();
		fecha = Calendar.getInstance();
		fecha.set(aleatorio.nextInt(10) + 2017, aleatorio.nextInt(12) + 1, aleatorio.nextInt(30) + 1);
		new SimpleDateFormat("dd/MM/yyyy");
		return fecha.getTime();
	}

	/**
	 * Obtiene una hora aleatoria.
	 * @return hora
	 */
	public static Date ObtenerHora() {
		Calendar hora;
		Random aleatorio;
		aleatorio = new Random();
		hora = Calendar.getInstance();
		hora.set(aleatorio.nextInt(10) + 2017, aleatorio.nextInt(12) + 1, aleatorio.nextInt(30) + 1);
		new SimpleDateFormat("hh/mm");
		return hora.getTime();
	}

}