package org.questgame.webquestgame.Logic.Elements;

import java.io.Serializable;

/**
 * A subclass of {@link Situation} representing failure ending of story (opposite to {@link Victory} class)
 * <p>The class implements {@link Serializable}, allowing its state to be saved and restored when needed.</p>
 * @see Situation
 * @see MainSituation
 * @see Choice
 */
public class Fail extends Situation implements Serializable {

	/**
	 * Constructor to create object with text description and connections from other choices
	 * @param description text description of the fail
	 * @param leadFrom an array of {@link Choice} objects that lead to this fail
	 */
	public Fail(String description, Choice[] leadFrom) {
		super(description, leadFrom);
	}

	/**
	 * Constructor to create object with text description and Connections from other choices
	 * @param description text description of the fail
	 */
	public Fail(String description) {
		super(description);
	}
}
