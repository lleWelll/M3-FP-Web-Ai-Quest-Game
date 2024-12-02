package org.questgame.webquestgame.Logic.Elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * An abstract class for describing story elements.
 *
 * <p>This class is used as a base for other plot elements, such as
 * {@link MainSituation}, {@link Situation}, {@link Victory}, {@link Fail} and {@link Choice}. It provides a public field
 * {@code description}, which contains a text description of the element.</p>
 *
 * <p>The class implements the {@link Serializable} interface, which allows the state of
 * descendant class objects to be saved and restored from a file or other stream.</p>
 *
 * @see MainSituation
 * @see Situation
 * @see Victory
 * @see Fail
 * @see Choice
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Element implements Serializable {

	/**
	 * Description of the element.
	 * Used for textual representation of the story element, for example for display in UI
	 */
	private String description;
}
