package org.questgame.webquestgame.Logic.Elements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.questgame.webquestgame.Logic.Interfaces.StepProcessor;

import java.io.Serializable;

/**
 * Represents a choice within the game, connecting two {@link MainSituation} instances.
 *
 * <p>The {@code Choice} class extends {@link Element} and implements {@link StepProcessor},
 * allowing navigation between game situations. Each choice can lead from one situation
 * to another based on the player's decision or a predefined logic.</p>
 *
 * <p>The class implements {@link Serializable}, making it suitable for saving and restoring
 * its state.</p>
 *
 * @see MainSituation
 * @see Situation
 * @see Element
 * @see StepProcessor
 */
@Getter
@Setter
@NoArgsConstructor
public class Choice extends Element implements StepProcessor<MainSituation>, Serializable {

	/**
	 * The {@link MainSituation} from which this choice originates.
	 */
	private MainSituation leadFrom;

	/**
	 * The {@link MainSituation} to which this choice leads.
	 */
	private MainSituation leadTo;

	/**
	 * Determines whether to move to {@code leadTo} or remain at {@code leadFrom}.
	 */
	private boolean goNext;

	/**
	 * Processes the step and determines the next situation based on the `goNext` field.
	 * @return the {@link MainSituation} to transition to:
	 *         - {@code leadTo} if {@code goNext} is {@code true};
	 *         - {@code leadFrom} otherwise.
	 */
	@Override
	public MainSituation goNext() {
		if (goNext) return leadTo;
		else return leadFrom;
	}

	/**
	 * Constructs a choice with a description, linked situations, and a decision flag.
	 *
	 * @param description a text description of the choice
	 * @param leadFrom the {@link Situation} from which the choice originates
	 * @param leadTo the {@link Situation} to which the choice leads
	 * @param goNext a boolean flag indicating whether to move to {@code leadTo} or remain at {@code leadFrom}
	 */
	public Choice(String description, Situation leadFrom, Situation leadTo, boolean goNext) {
		super(description);
		this.leadFrom = leadFrom;
		this.leadTo = leadTo;
		this.goNext = goNext;
	}

	/**
	 * Constructs a choice with a description and a decision flag.
	 * <p>This constructor is useful when the linked situations are not yet defined.</p>
	 * @param description a text description of the choice
	 * @param goNext a boolean flag indicating whether the choice should lead to the next situation
	 */
	public Choice(String description, boolean goNext) {
		super(description);
		this.goNext = goNext;
	}
}
