package ro.per.online.jsf.scope;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * 
 * Permite simular el scope View en Spring.
 * 
 * @author STAD
 *
 */
public class FacesViewScope implements Scope {

	/**
	 * Nombre del scope.
	 */
	public static final String NAME = "view";

	/**
	 * 
	 */
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			throw new IllegalStateException("FacesContext.getCurrentInstance() returned null");
		}

		Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();

		Object respuesta;
		if (viewMap.containsKey(name)) {
			respuesta = viewMap.get(name);
		}
		else {
			Object object = objectFactory.getObject();
			viewMap.put(name, object);

			respuesta = object;
		}
		return respuesta;
	}

	/**
	 * 
	 */
	@Override
	public Object remove(String name) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
	}

	/**
	 * 
	 */
	@Override
	public String getConversationId() {
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// Not supported by JSF for view scope
	}

	/**
	 * 
	 */
	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}
}
