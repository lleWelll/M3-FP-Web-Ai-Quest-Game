package org.questgame.webquestgame.Logic.Elements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;

/**
 * A subclass of {@link Element} representing a main situation in the game story.
 * <p>This class describes a specific scenario in the game where a player can make decisions.
 * Each situation may include an array of {@link Choice} objects that define possible options
 * for the player.</p>
 * <p>The class implements {@link Serializable}, allowing its state to be saved and restored
 * when needed.</p>
 * @see Element
 * @see Choice
 */
@Getter
@NoArgsConstructor
public class MainSituation extends Element implements Serializable {

	/**
	 * An array of available choices in this situation.
	 */
	private Choice[] choices;

	/**
	 * Adds a new choice to the array of available options.
	 * @param choice the {@link Choice} object to be added
	 * @throws IllegalArgumentException when Argument {@code choice} is null
	 */
	public void addChoice(Choice choice) {
		if (choice == null) throw new IllegalArgumentException("Choice cannot be null");
		choices = ArrayUtils.add(choices, choice);
	}

	/**
	 * Constructor to create a situation with a description and a set of choices.
	 * @param description a text description of the situation
	 * @param choices an array of {@link Choice} objects representing possible options
	 */
	public MainSituation(String description, Choice[] choices) {
		super(description);
		this.choices = choices;
	}

	/**
	 * Constructor to create a situation with only a description.
	 * <p>This constructor is useful when the choices for the situation are not yet defined.</p>
	 * @param description a text description of the situation
	 */
	public MainSituation(String description) {
		super(description);
	}
}
