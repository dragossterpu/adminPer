package ro.per.online.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import ro.per.online.persistence.entities.enums.CivilStatusEnum;
import ro.per.online.persistence.entities.enums.EducationEnum;
import ro.per.online.persistence.entities.enums.SexEnum;

/**
 *
 * Entitate aditionala pentru completarea datelor utilizatorului..
 *
 * @author STAD
 *
 */
@Getter
@Setter
@Embeddable
public class PersonalData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Email personal.
	 */
	private String personalEmail;

	/**
	 * Cuerpo al que pertenece el usuario.
	 */
	@ManyToOne
	@JoinColumn(name = "PROVINCE_ID", foreignKey = @ForeignKey(name = "FK_U_PROVINCE"))
	private PProvince province;

	/**
	 * Localitatea.
	 */
	@ManyToOne
	@JoinColumn(name = "LOCALITY_ID", foreignKey = @ForeignKey(name = "FK_U_LOCALITY"))
	private PLocality locality;

	/**
	 * Telefonul.
	 */
	private String phone;

	/**
	 * Adresa.
	 */
	private String address;

	/**
	 * CNP utilizator.
	 */
	private String idCard;

	/**
	 * Numar buletin de identitate utilizator.
	 */
	private String numberCard;

	/**
	 * Data nasterii utilizator.
	 */
	private Date birthDate;

	/**
	 * Educatie utilizator.
	 */
	@Column(name = "education")
	@Enumerated(EnumType.STRING)
	private EducationEnum education;

	/**
	 * Loc de munca utilizator.
	 */
	private String workplace;

	/**
	 * Sex utilizator.
	 */
	@Column(name = "sex")
	@Enumerated(EnumType.STRING)
	private SexEnum sex;

	/**
	 * Stare civila utilizator.
	 */
	@Column(name = "civilStatus")
	@Enumerated(EnumType.STRING)
	private CivilStatusEnum civilStatus;

	/**
	 * Fotoografia utilizator.
	 */
	private Byte photo;

	/**
	 * Utilizator validat.
	 */
	private Boolean validated;
}
