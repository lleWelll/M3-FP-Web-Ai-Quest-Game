package org.questgame.tests.Elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Logic.Elements.Choice;
import org.questgame.webquestgame.Logic.Elements.Situation;

@ExtendWith(MockitoExtension.class)
public class SituationTest {

	private Situation situation;

	@Mock
	private Choice choice1, choice2, choice3;

	@BeforeEach
	public void init() {
		Mockito.lenient().when(choice1.getDescription()).thenReturn("Choice 1 test description");
		Mockito.lenient().when(choice2.getDescription()).thenReturn("Choice 2 test description");
		Mockito.lenient().when(choice3.getDescription()).thenReturn("Choice 3 test description");
		situation = new Situation("Test description", new Choice[] {choice1, choice2});
	}

	@Test
	public void getLeadFrom_withTwoChoices_shouldReturnAllChoices() {
		String actual = "";
		String expected = choice1.getDescription() + " " + choice2.getDescription();

		for (Choice c : situation.getLeadFrom()) {
			actual += c.getDescription() + " ";
		}
		actual = actual.trim();
		expected = expected.trim();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void addLeadFrom_withCorrectChoice_shouldSuccessfullyAddNewChoice() {
		int actualSize;
		int expectedSize = 3;

		situation.addLeadFrom(choice3);
		actualSize = situation.getLeadFrom().length;

		Assertions.assertEquals(expectedSize, actualSize);
	}

	@ParameterizedTest
	@NullSource
	public void addLeadFrom_whenChoiceIsNull_shouldThrowIllegalArgumentException(Choice choice) {
		Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> situation.addLeadFrom(choice)
		);
	}
}
