package ro.per.online.persistence.entities.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Defineste nivelul de educatie al utilizatorului
 *
 * @author STAD
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EducationEnum {
	/**
	 * Bazic .
	 */
	BASIC("Bazic"),

	/**
	 * Liceu.
	 */
	HIGHSCHOOL("Liceu"),

	/**
	 * Studii universitare.
	 */
	FACULTY("Studii universitare"),

	/**
	 * Nespecificat.
	 */
	UNSPECIFIED("Nespecificat");

	/**
	 * Descrierea enum.
	 */
	private String description;

	/**
	 * @return Returnează o listă cu descrierile enum.
	 *
	 */
	public static List<EducationEnum> getEducation() {
		List<EducationEnum> educations = new ArrayList<>();
		for (EducationEnum education : EducationEnum.values()) {
			educations.add(education);
		}
		return educations;
	}

	/**
	 * @return Numele enum-ului "
	 */
	public String getName() {
		return name();
	}
}
