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
import org.questgame.webquestgame.Logic.Elements.MainSituation;

@ExtendWith(MockitoExtension.class)
public class MainSituationTest {

	private MainSituation mainSituation;

	@Mock
	Choice choice1, choice2, choice3;

	@BeforeEach
	public void init() {
		Mockito.lenient().when(choice1.getDescription()).thenReturn("Choice 1 test description");
		Mockito.lenient().when(choice2.getDescription()).thenReturn("Choice 2 test description");
		Mockito.lenient().when(choice3.getDescription()).thenReturn("Choice 3 test description");
		mainSituation = new MainSituation("Test description", new Choice[]{choice1, choice2});
	}

	@Test
	public void getDescription_shouldReturnDescription() {
		String expected = "Test description";
		Assertions.assertEquals(expected, mainSituation.getDescription());
	}

	@Test
	public void setDescription_shouldSuccessfullySetNewDescription() {
		String expected = "This is new description";
		mainSituation.setDescription("This is new description");
		Assertions.assertEquals(expected, mainSituation.getDescription());
	}

	@Test
	public void getChoices_withOneChoice_shouldReturnAllChoices() {
		mainSituation = new MainSituation("Test description", new Choice[]{choice1});
		String actual = "";
		String expected = choice1.getDescription();

		for (Choice c : mainSituation.getChoices()) {
			actual += c.getDescription() + " ";
		}
		expected = expected.trim();
		actual = actual.trim();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void getChoices_withTwoChoices_shouldReturnAllChoices() {
		String actual = "";
		String expected = choice1.getDescription() + " " + choice2.getDescription();

		for (Choice c : mainSituation.getChoices()) {
			actual += c.getDescription() + " ";
		}
		expected = expected.trim();
		actual = actual.trim();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void getChoice_withThreeChoices_shouldReturnAllChoices() {
		mainSituation = new MainSituation("Test description", new Choice[]{choice1, choice2, choice3});
		String actual = "";
		String expected = choice1.getDescription() + " " + choice2.getDescription() + " " + choice3.getDescription();

		for (Choice c : mainSituation.getChoices()) {
			actual += c.getDescription() + " ";
		}
		expected = expected.trim();
		actual = actual.trim();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void getChoice_whenChoicesAreNull_shouldThrowNullPointerException() {
		mainSituation = new MainSituation("Test description");
		Choice[] choices = mainSituation.getChoices();

		Assertions.assertAll(
				() -> Assertions.assertNull(choices),
				() -> Assertions.assertThrows(
						NullPointerException.class,
						() -> {
							for (Choice c : choices) {
								c.getDescription();
							}
						})
		);
	}
	
	@Test
	public void addChoice_whenAddingCorrectObject_shouldSuccessfullyAddNewChoice() {
		int actualSize;
		int expectedSize = 3;
		
		mainSituation.addChoice(choice3);
		actualSize = mainSituation.getChoices().length;
		
		Assertions.assertEquals(expectedSize, actualSize);
	}

	@ParameterizedTest
	@NullSource
	public void addChoice_whenChoiceIsNull_shouldThrowIllegalArgumentException(Choice choice) {
		Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> {
					mainSituation.addChoice(choice);
				}
		);
	}
}
