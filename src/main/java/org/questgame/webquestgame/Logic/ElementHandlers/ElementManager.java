package org.questgame.webquestgame.Logic.ElementHandlers;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.Elements.*;
import org.questgame.webquestgame.Logic.Story;

import java.util.Stack;

/**
 * The {@code ElementManager} class is responsible for managing the flow of game elements within a story,
 * including navigating between situations, retrieving choices, and maintaining the current state of the game.
 * <p>
 * It uses a stack-based structure to track the progression of {@link MainSituation} elements, allowing the user
 * to move between the main situation, current situation, and previous or next situations as required.
 * </p>
 *
 * @see Story
 * @see MainSituation
 * @see Choice
 */
@Getter
@Setter
public class ElementManager {

	/**
	 * Logger instance for recording execution details.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * The story object containing all game elements.
	 */
	private final Story STORY;

	/**
	 * A stack that maintains the navigation history of {@link MainSituation} elements.
	 * The stack ensures that the current situation is always on top, while allowing
	 * navigation back to previous situations.
	 */
	private final Stack<MainSituation> elementStack = new Stack<>();

	/**
	 * Retrieves the current situation from the top of the stack.
	 * @return the current {@link MainSituation}
	 */
	public MainSituation getCurrentSituation() {
		log.debug("Returning current situation ({})", elementStack.peek().getDescription());
		return elementStack.peek();
	}

	/**
	 * Retrieves the previous situation from the stack.
	 * If the stack contains only one situation, it returns the main situation (with index S1).
	 * @return the previous {@link MainSituation}
	 */
	public MainSituation getPreviousSituation() {
		if (elementStack.size() <= 1) {
			return getMainSituation();
		}
		elementStack.pop();
		log.debug("Returning previous situation ({})", elementStack.peek().getDescription());
		return elementStack.peek();
	}

	/**
	 * Retrieves the next situation based on the choice index provided.
	 * This method pushes the next situation onto the stack to maintain navigation history.
	 * @param index the index of the choice leading to the next situation
	 * @return the next {@link MainSituation}
	 */
	public MainSituation getNextSituation(int index) {
		if (index > getCurrentChoices().length) throw new IllegalArgumentException("Index is invalid, " + index + " > " + getCurrentChoices().length);
		MainSituation next = elementStack.peek().getChoices()[index].goNext();
		elementStack.push(next);
		log.debug("Returning next situation ({})", next.getDescription());
		return elementStack.peek();
	}

	/**
	 * Retrieves the main situation of the story, which is always associated with the index "S1".
	 * This method pushes the main situation onto the stack.
	 * @return the main {@link MainSituation}
	 */
	public MainSituation getMainSituation() {
		elementStack.push((MainSituation) STORY.getSTORY_ELEMENTS().get("S1"));
		log.debug("Returning Main Situation ({})", elementStack.peek().getDescription());
		return elementStack.peek();
	}

	/**
	 * Retrieves the choices available in the current situation.
	 * @return an array of {@link Choice} objects representing the available choices
	 */
	public Choice[] getCurrentChoices() {
		log.debug("Returning choices for Situation {}", elementStack.peek().getDescription());
		return elementStack.peek().getChoices();
	}

	/**
	 * Constructs an {@code ElementManager} with the specified story object.
	 * The story object contains all the elements necessary for gameplay.
	 * @param STORY the {@link Story} object containing the game elements
	 */
	public ElementManager(Story STORY) {
		log.info("Element Manager created");
		this.STORY = STORY;
	}
}
