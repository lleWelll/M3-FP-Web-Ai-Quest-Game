package org.questgame.webquestgame.Logic.Elements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;

/**
 * A subclass of {@link MainSituation} representing a situation that connects to other choices.
 * <p>This class extends the functionality of {@link MainSituation} by introducing a `leadFrom`
 * field, which keeps track of choices that lead to this situation. It enables the representation
 * of complex decision trees in the game story.</p>
 * <p>The class implements {@link Serializable}, allowing its state to be saved and restored when needed.</p>
 *
 * @see MainSituation
 * @see Choice
 */
@Getter
@NoArgsConstructor
public class Situation extends MainSituation implements Serializable {

	/**
	 * An array of choices that lead to this situation.
	 */
	private Choice[] leadFrom;

	/**
	 * Adds a new choice to the array of choices that lead to this situation.
	 * @param choice the {@link Choice} object to be added. If {@code null}, the operation is ignored.
	 */
	public void addLeadFrom(Choice choice) {
		if (choice == null) throw new IllegalArgumentException("Choice can't be null");
		leadFrom = ArrayUtils.add(leadFrom, choice);
	}

	/**
	 * Constructor to create a situation with a description, choices, and connections from other choices.
	 * @param description a text description of the situation
	 * @param choices     an array of {@link Choice} objects representing possible options in the situation
	 * @param leadFrom    an array of {@link Choice} objects that lead to this situation
	 */
	public Situation(String description, Choice[] choices, Choice[] leadFrom) {
		super(description, choices);
		this.leadFrom = leadFrom;
	}

	/**
	 * Constructor to create a situation with only a description.
	 * <p>This constructor is useful when choices and connections are not yet defined.</p>
	 * @param description a text description of the situation
	 */
	public Situation(String description) {
		super(description);
	}

	/**
	 * Constructor to create a situation with a description and connections from other choices.
	 * @param description a text description of the situation
	 * @param leadFrom     an array of {@link Choice} objects that lead to this situation
	 */
	public Situation(String description, Choice[] leadFrom) {
		super(description);
		this.leadFrom = leadFrom;
	}
}
