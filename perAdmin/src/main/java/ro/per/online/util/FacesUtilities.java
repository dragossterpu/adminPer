package ro.per.online.util;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * Clase con utilidades para usar con primefaces.
 * 
 * @author STAD
 *
 */
@Component("facesUtilities")
public class FacesUtilities {

	/**
	 * Redirige a la página pasada como parámetro.
	 * 
	 * @author STAD
	 * @param pagina deseada
	 */
	public void redirect(final String pagina) {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		final ExternalContext extContext = ctx.getExternalContext();
		final String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, pagina));
		try {
			extContext.redirect(url);
		}
		catch (IOException ioe) {
			throw new FacesException(ioe);
		}
	}

	/**
	 * Redirige a la página pasada como parámetro añadiendo parámetros GET a evaluar en destino.
	 * 
	 * @author STAD
	 * @param pagina deseada
	 * @param paramGET cadena con parametros que se quieran pasar a la página destino separados por '&amp;'
	 */
	public void redirect(final String pagina, final String paramGET) {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		final ExternalContext extContext = ctx.getExternalContext();

		final String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, pagina));

		try {
			extContext.redirect(url + "?" + paramGET);
		}
		catch (IOException ioe) {
			throw new FacesException(ioe);
		}
	}

	/**
	 * Muestra una cuadro de diálogo con información. El cuadro de diálogo debe tener como nombre (widgetVar)
	 * "dialogMessage".
	 * 
	 * @author STAD
	 * @param severity gravedad del aviso
	 * @param summary resumen
	 * @param detail detalles del mensaje
	 */
	public static void setMensajeConfirmacionDialog(final Severity severity, final String summary,
			final String detail) {
		setMensajeConfirmacionDialog(severity, summary, detail, "dialogMessage");
	}

	/**
	 * Muestra una cuadro de diálogo con información.
	 * 
	 * @author STAD
	 * @param severity gravedad del aviso
	 * @param summary resumen
	 * @param detail detalles del mensaje
	 * @param widgetVarName Nombre del cuadro de diálogo
	 */
	public static void setMensajeConfirmacionDialog(final Severity severity, final String summary, final String detail,
			final String widgetVarName) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(widgetVarName, message);
		context.execute("PF('" + widgetVarName + "').show()");
	}

	/**
	 * Muestra un mensaje por pantalla.
	 * 
	 * @author STAD
	 * @param severity gravedad del aviso
	 * @param summary resumen
	 * @param detail detalles del mensaje
	 * @param idMensaje identificador del componente "message/s" de PrimeFaces donde se desea mostrar
	 */
	public static void setMensajeInformativo(final Severity severity, final String summary, final String detail,
			final String idMensaje) {
		final FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(idMensaje, message);
	}

}
