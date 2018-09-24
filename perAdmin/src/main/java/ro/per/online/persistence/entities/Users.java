package ro.per.online.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ro.per.online.persistence.entities.enums.RoleEnum;

/**
 *
 * Entidad para el almacenamiento de un Users.
 *
 * @author STAD
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "username")
@Builder
@ToString
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class Users extends AbstractEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7888266252869220306L;

	/**
	 * Login (ID).
	 */
	@Id
	@Column(name = "username", length = 150, nullable = false)
	private String username;

	@Embedded
	private PersonalData personalData;

	/**
	 * Parola utilizatorlui.
	 */
	@Column(name = "password", length = 100, nullable = false)
	private String password;

	/**
	 * Rolul utilizatorlui.
	 */
	@Column(name = "role", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	/**
	 * Numele utilizatorlui.
	 */
	@Column(name = "name", length = 100, nullable = false)
	private String name;

	/**
	 * Prenumele utilizatorlui.
	 */
	@Column(name = "last_name", length = 100, nullable = false)
	private String lastName;

	/**
	 * Email.
	 */
	@Column(name = "email", length = 100, nullable = false)
	private String email;

	/**
	 * Devuelve el nombre completo del usuario.
	 * 
	 * @return Cadena formada por la concatenación de nombre y apellidos del usuario
	 */
	public String getNombreCompleto() {
		StringBuilder nombreCompleto = new StringBuilder();
		nombreCompleto.append(name);
		nombreCompleto.append(' ');
		nombreCompleto.append(lastName);
		return nombreCompleto.toString();
	}
}
