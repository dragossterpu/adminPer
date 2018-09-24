
package ro.per.online.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.context.FacesContext;

/**
 * Métodos de utilidades.
 * 
 * @author STAD
 *
 */
public class Utilities {

	/**
	 * Las clases de utilidad no deben tener constructor publico
	 *
	 */
	private Utilities() {
		// throw new IllegalStateException("Utiles class");
	}

	/**
	 * Converts a Map to a List filled with its entries. This is needed since very few if any JSF iteration components
	 * are able to iterate over a map.
	 */
	public static <T, S> List<Map.Entry<T, S>> mapToList(Map<T, S> map) {

		if (map == null) {
			return null;
		}

		final List<Map.Entry<T, S>> list = new ArrayList<Map.Entry<T, S>>();
		list.addAll(map.entrySet());

		return list;
	}

	/**
	 * Comprueba si un objeto es nulo o blanco.
	 * @param obj El objeto a comprobar.
	 * @return true, Si el objeto es nulo o su valor en String es blanco.
	 */
	public static boolean isNullOrBlank(final Object obj) {
		if (obj == null) {
			return true;
		}
		if ("".equals(obj.toString())) {
			return true;
		}
		return false;
	}

	// ************* Generating new password PER ********************//
	/**
	 * Devuelve una contraseña basada en una combinación de letras y números.
	 * 
	 * @author STAD
	 * @return contraseña
	 */
	public static String getPassword() {
		return getPinLetters() + getPinNumber();
	}

	/**
	 * Recupera una cadena de cuatro números al azar.
	 * 
	 * @author STAD
	 * @return números
	 */
	public static String getPinNumber() {
		return getRandomChars("0123456789", 4);
	}

	/**
	 * Recupera una cadena de cuatro letras al azar.
	 * 
	 * @author STAD
	 * @return letras
	 */
	public static String getPinLetters() {
		return getRandomChars("ABCDEFGHIJKLMNOPRSTUVXYZWQ", 4);
	}

	/**
	 * Recupera una cadena de caracteres aleatorios a partir de un conjunto de caracteres y una longitud determinada.
	 * 
	 * @author STAD
	 * @param key cadena de caracteres posibles
	 * @param length número de caracteres deseados
	 * @return cadena de caracteres elegidos
	 */
	public static String getRandomChars(String key, int length) {
		StringBuilder pswd = new StringBuilder();
		Random r = new Random();

		for (int i = 0; i < length; i++) {
			pswd.append(key.charAt(r.nextInt(key.length())));
		}

		return pswd.toString();
	}

	// ************* Generating new password PER END ********************//

	/**
	 * Genera un mensaje de error a partir de una excepción.
	 * 
	 * @param e Excepción
	 * @return Mensaje
	 */
	public static String messageError(Exception e) {
		String message = Arrays.toString(e.getStackTrace());
		if (message.length() > 2000) {
			message = message.substring(message.length() - 2000);
		}
		return message;
	}

	/**
	 * Formatea una fecha según un patrón recibido como parámetro.
	 * 
	 * @param date fecha a formatear
	 * @param pattern patrón de fecha
	 * @return fecha formateada
	 */

	public static String getFechaFormateada(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * Devuelve el número de días que han pasado desde una fecha introducida por parámetro hasta hoy.
	 * 
	 * @param fecha usuario a consultar
	 * @return dias número de días
	 */
	public static Long getDiasHastaHoy(final Date fecha) {
		LocalDate hoy = LocalDate.now();
		long dias = 0;
		LocalDate fechaDesde = null;
		if (fecha != null) {
			fechaDesde = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			dias = ChronoUnit.DAYS.between(fechaDesde, hoy);
		}
		return dias;
	}

	/**
	 * Método para evitar los nulos en cadenas de texto.
	 * @param cadena de entrada
	 * @return cadena de salida
	 */
	public static String nullToBlank(final String cadena) {
		String cad = cadena;
		if (cad == null) {
			cad = "";
		}
		return cad;
	}

	/**
	 * Elimina los bean de la sesión que no contienen ese nombre.
	 * 
	 * @param nombreBeanActual Nombre del bean que no se tiene que eliminar de la sesión.
	 */
	public static void cleanSession(String nombreBeanActual) {
		final Map<String, Object> mapaSesion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		for (final String cabecera : mapaSesion.keySet()) {
			final String ubicacion = mapaSesion.get(cabecera).getClass().getPackage().toString().toLowerCase();
			if (ubicacion.contains("bean") && "navegacionBean".equals(cabecera) == Boolean.FALSE
					&& cabecera.equals(nombreBeanActual) == Boolean.FALSE) {
				mapaSesion.remove(cabecera);
			}
		}
	}

	/**
	 * Elimina los bean de la sesión que no contienen ese nombre.
	 * 
	 * @param listaBeans Nombre de los beans que no se tienen que eliminar de la sesión.
	 */
	public static void cleanSession(List<String> listaBeans) {
		final Map<String, Object> mapaSesion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		for (final String cabecera : mapaSesion.keySet()) {
			final String ubicacion = mapaSesion.get(cabecera).getClass().getPackage().toString().toLowerCase();
			if (ubicacion.contains("bean") && "navegacionBean".equals(cabecera) == Boolean.FALSE
					&& listaBeans.contains(cabecera) == Boolean.FALSE) {
				mapaSesion.remove(cabecera);
			}
		}
	}
}